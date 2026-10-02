package hu.katalin.minispring;

import java.lang.reflect.Constructor;
import java.util.Set;

public class Application {

    public static void main(String[] args) {
        System.out.println("--- Mini-Spring Container Starting ---");

        // 1. Initialize our scanner
        ComponentScanner scanner = new ComponentScanner();

        // 2. Scan the base package
        System.out.println("Scanning package: hu.katalin.minispring");
        Set<Class<?>> componentClasses = scanner.scan("hu.katalin.minispring");

        System.out.println("Found " + componentClasses.size() + " component(s).");

        // 3. Iterate through found components and instantiate them
        for (Class<?> clazz : componentClasses) {
            System.out.println("Processing class: " + clazz.getSimpleName());
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                Object instance = constructor.newInstance();

                // Just to prove it works dynamically (if it's the UserService, call doWork)
                if (instance instanceof UserService) {
                    ((UserService) instance).doWork();
                }
            } catch (Exception e) {
                System.err.println("Failed to instantiate " + clazz.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("--- Mini-Spring Container Started ---");
    }
}