package hu.katalin.minispring;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationContext {

    // 1. Standard Java Logger  (Enterprise best practice)
    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

    // The repository: The Key is the Class type, and the Value is the Object.
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    // All the classes found that will be used to create a bean
    private final Set<Class<?>> componentClasses;

    public ApplicationContext(String basePackage) {
        logger.info("Starting Mini-Spring ApplicationContext DI Engine...");

        // Scan for components
        ComponentScanner scanner = new ComponentScanner();
        this.componentClasses = scanner.scan(basePackage);

        // We'll go through all the components we've found, and if any aren't already in the repository, we'll build them
        for (Class<?> clazz : componentClasses) {
            if (!beans.containsKey(clazz)) {
                createBean(clazz);
            }
        }
    }

    /**
     * RECURSIVE DEPENDENCY RESOLUTION ALGORITHM (Dependency Graph Resolver)
     */
    private Object createBean(Class<?> clazz) {
        // If we've already created this bean recursively because of another class, just return it
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        try {
            // 1. Find the matching constructor (either the one with @MyAutowired or the first one)
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            Constructor<?> constructorToUse = constructors[0];

            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(MyAutowired.class)) {
                    constructorToUse = constructor;
                    break;
                }
            }

            // 2. We collect the types of the constructor's parameters (the dependencies!)
            Class<?>[] parameterTypes = constructorToUse.getParameterTypes();
            Object[] parameterInstances = new Object[parameterTypes.length];

            // 3. RECURSION: We must have the container create each dependency
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                logger.info("Resolving dependency: " + paramType.getSimpleName() + " for " + clazz.getSimpleName());

                // Build the dependency (if it isn't already built)
                Object dependency = createBean(paramType);
                parameterInstances[i] = dependency;
            }

            // 4. CREATION: Now that we have all the dependencies, we'll instantiate the object
            Object instance = constructorToUse.newInstance(parameterInstances);

            // 5. REGISTRATION: Added to the repository
            beans.put(clazz, instance);
            logger.info("Successfully created and injected bean: " + clazz.getSimpleName());

            return instance;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to instantiate bean for class: " + clazz.getName(), e);
            throw new RuntimeException("DI Failure", e);
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