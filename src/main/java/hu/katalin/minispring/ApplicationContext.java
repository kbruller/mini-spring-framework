package hu.katalin.minispring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationContext {

    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

    // The Registry for singleton beans
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    // Set to keep track of beans that are currently being created (Cycle detection!)
    private final Set<Class<?>> currentlyCreating = new HashSet<>();

    public ApplicationContext(String basePackage) {
        logger.info("Starting Mini-Spring ApplicationContext DI Engine...");

        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> componentClasses = scanner.scan(basePackage);

        for (Class<?> clazz : componentClasses) {
            if (!beans.containsKey(clazz)) {
                createBean(clazz);
            }
        }
    }

    private Object createBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        // --- CYCLE DETECTION START ---
        if (currentlyCreating.contains(clazz)) {
            throw new CircularDependencyException("Circular dependency detected for class: " + clazz.getName());
        }

        // Add to the "in progress" set
        currentlyCreating.add(clazz);
        // --- CYCLE DETECTION END ---

        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> constructorToUse = constructors[0];

            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(MyAutowired.class)) {
                    constructorToUse = constructor;
                    break;
                }
            }

            Class<?>[] parameterTypes = constructorToUse.getParameterTypes();
            Object[] parameterInstances = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                logger.info("Resolving dependency: " + paramType.getSimpleName() + " for " + clazz.getSimpleName());

                Object dependency = createBean(paramType);
                parameterInstances[i] = dependency;
            }

            // 1. We create the ORIGINAL (Target) object
            Object instance = constructorToUse.newInstance(parameterInstances);

            // --- AOP WEAVING START ---
            // Let's check whether the class implements any interfaces.
            // The JDK Dynamic Proxy works ONLY with interfaces!
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                // We wrap the original object in our AopProxyHandler
                instance = Proxy.newProxyInstance(
                        clazz.getClassLoader(),
                        interfaces,
                        new AopProxyHandler(instance)
                );
                logger.info("Wrapped bean in AOP Proxy: " + clazz.getSimpleName());
            }
            // --- AOP WEAVING END ---

            // 2. IMPORTANT: We are going to put the PROXY into the Warehouse here, not the original object!
            beans.put(clazz, instance);
            logger.info("Successfully created AOP Proxy: " + instance.getClass().getSimpleName());

            return instance;

        } catch (CircularDependencyException e) {
            // Rethrow the circular dependency exception as is, so it doesn't get buried
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to instantiate bean for class: " + clazz.getName(), e);
            throw new RuntimeException("DI Failure", e);
        } finally {
            // --- CLEANUP ---
            // Whether it succeeded or failed, remove it from the "in progress" set
            currentlyCreating.remove(clazz);
        }
    }

    /**
     * Retrieves a bean from the container.
     * Now supports Polymorphism (requesting an Interface and getting the Implementation/Proxy).
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requestedType) {

        // 1. Quick Search: Is there a complete match for the key?
        Object bean = beans.get(requestedType);
        if (bean != null) {
            return (T) bean;
        }

        // 2. Polymorphic search: We scan the Repository to see if there is a stored Class
        // that implements the requested Interface or is a subclass of the requested Base Class.
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Class<?> registeredClass = entry.getKey();

            if (requestedType.isAssignableFrom(registeredClass)) {
                return (T) entry.getValue();
            }
        }

        // If we've gone through everything and there isn't one, then the bean is really missing.
        throw new RuntimeException("No bean found in registry for type: " + requestedType.getName());
    }
}