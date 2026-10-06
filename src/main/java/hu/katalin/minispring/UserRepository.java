package hu.katalin.minispring;

@MyComponent
public class UserRepository {
    
    public UserRepository() {
        System.out.println("-> [UserRepository] Instance created.");
    }

    public void saveUser() {
        System.out.println("-> [UserRepository] Saving user to the database...");
    }
}