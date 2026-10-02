package hu.katalin.minispring;

public class Application {

    public static void main(String[] args) {
        // 1. Boot up the IoC container (this will scan and instantiate everything)
        ApplicationContext context = new ApplicationContext("hu.katalin.minispring");

        System.out.println("\n--- Application is Running ---\n");

        // 2. Ask the container for the UserService bean
        UserService userService = context.getBean(UserService.class);

        // 3. Ask it again! (To prove it's a Singleton, it will return the exact same instance)
        UserService userService2 = context.getBean(UserService.class);

        // 4. Verify they are the same object in memory
        System.out.println("Are both instances the same object in memory? " + (userService == userService2));

        // 5. Execute business logic
        userService.doWork();
    }
}