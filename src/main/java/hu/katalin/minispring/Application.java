package hu.katalin.minispring;

import java.lang.reflect.Constructor;

public class Application {

    public static void main(String[] args) {
        System.out.println("--- Mini-Spring Container Starting ---");

        // 1. Obtain the Class object (metadata) for the target class
        Class<?> targetClass = UserService.class;

        // 2. Use Reflection to check if our custom annotation is present
        if (targetClass.isAnnotationPresent(MyComponent.class)) {
            System.out.println("Success! Found @MyComponent on class: " + targetClass.getSimpleName());
            
            try {
                // 3. Retrieve the default (no-args) constructor via Reflection
                Constructor<?> constructor = targetClass.getDeclaredConstructor();
                
                // 4. CREATE THE INSTANCE dynamically without using the 'new' keyword in the business logic
                Object instance = constructor.newInstance();
                
                // 5. Cast the instance and invoke a method to verify it works
                UserService userService = (UserService) instance;
                userService.doWork();

            } catch (Exception e) {
                System.err.println("Error during instantiation: " + e.getMessage());
            }
        } else {
            System.out.println("Annotation @MyComponent not found.");
        }
    }
}