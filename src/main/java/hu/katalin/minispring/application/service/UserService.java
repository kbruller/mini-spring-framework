package hu.katalin.minispring.application.service;

import hu.katalin.minispring.framework.annotation.MyComponent;

@MyComponent
public class UserService {
    
    public UserService() {
        System.out.println("-> [UserService] Constructor executed! Instance created.");
    }

    public void doWork() {
        System.out.println("-> [UserService] doWork() method is executing...");
    }
}