package hu.katalin.minispring;

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