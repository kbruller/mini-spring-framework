package hu.katalin.minispring;

@MyComponent
public class UserService {
    
    public UserService() {
        System.out.println("-> [UserService] Constructor executed! Instance created.");
    }

    public void doWork() {
        System.out.println("-> [UserService] doWork() method is executing...");
    }
}