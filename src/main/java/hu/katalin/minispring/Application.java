package hu.katalin.minispring;

import hu.katalin.minispring.application.service.OrderService;
import hu.katalin.minispring.framework.core.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        // 1. Boot up the IoC container (this will scan and instantiate everything)
        ApplicationContext context = new ApplicationContext("hu.katalin.minispring.application");

        System.out.println("\n--- Application is Running ---\n");

        // 2. Ask the container for the OrderService bean
        // IMPORTANT: We request the Interface from the Repository, because the Proxy implements it!
        OrderService orderService = context.getBean(OrderService.class);

        System.out.println("Executing placeOrder()...");

        // This call really goes directly to the proxy (to the `invoke` method of `AopProxyHandler`)!
        orderService.placeOrder();
    }
}