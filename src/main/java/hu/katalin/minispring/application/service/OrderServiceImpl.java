package hu.katalin.minispring.application.service;

import hu.katalin.minispring.framework.annotation.MyComponent;
import hu.katalin.minispring.framework.annotation.MyTransactional;

@SuppressWarnings("unused")
@MyComponent
public class OrderServiceImpl implements OrderService {

    public OrderServiceImpl() {
        System.out.println("-> [OrderServiceImpl] Instance created.");
    }

    @Override
    @MyTransactional // That's where we put the magic!
    public void placeOrder() {
        System.out.println("-> [OrderServiceImpl] Executing business logic: Saving order to database...");
    }
}