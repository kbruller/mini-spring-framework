package hu.katalin.minispring;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The core IoC Container.
 * It manages the lifecycle and storage of all beans (components) in the application.
 */
public class ApplicationContext {

    // 1. Standard Java Logger  (Enterprise best practice)
    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

    // The repository: The Key is the Class type, and the Value is the Object.
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    /**
     * Constructor that boots up the container.
     */
    public ApplicationContext(String basePackage) {
        logger.info("Starting Mini-Spring ApplicationContext...");

        // Scan for components
        ComponentScanner scanner = new ComponentScanner();
        Set<Class<?>> componentClasses = scanner.scan(basePackage);

        // Instantiate and register beans
        for (Class<?> clazz : componentClasses) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                Object instance = constructor.newInstance();

                beans.put(clazz, instance);

                logger.info("Registered bean: " + clazz.getSimpleName());
            } catch (Exception e) {
                // 2. Itt cseréltük le a printStackTrace-t egy profi ERROR (SEVERE) logra,
                // ami átadja magát a kivételt (e) is a loggernek, így a stack trace is megmarad, de strukturáltan!
                logger.log(Level.SEVERE, "Failed to instantiate bean for class: " + clazz.getName(), e);
            }
        }
    }

    /**
     * Retrieves a bean from the container by its class type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        T bean = (T) beans.get(clazz);

        if (bean == null) {
            throw new RuntimeException("No bean found for class: " + clazz.getName());
        }

        return bean;
    }
}