package hu.katalin.minispring.application.repository;

import hu.katalin.minispring.framework.annotation.MyComponent;

@MyComponent
public class UserRepository {
    
    public UserRepository() {
        System.out.println("-> [UserRepository] Instance created.");
    }

    public void saveUser() {
        System.out.println("-> [UserRepository] Saving user to the database...");
    }
}