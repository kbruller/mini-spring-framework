package hu.katalin.minispring;

import java.lang.reflect.Constructor;
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
        // IDE Warning fixed: componentClasses is now a local variable!
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

            Object instance = constructorToUse.newInstance(parameterInstances);

            beans.put(clazz, instance);
            logger.info("Successfully created and injected bean: " + clazz.getSimpleName());

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

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        T bean = (T) beans.get(clazz);
        if (bean == null) {
            throw new RuntimeException("No bean found in registry for class: " + clazz.getName());
        }
        return bean;
    }
}