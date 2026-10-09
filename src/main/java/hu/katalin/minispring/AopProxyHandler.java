package hu.katalin.minispring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * The core of our AOP engine.
 * It intercepts method calls made to the proxy and adds Cross-Cutting Concerns (like transactions).
 */
public class AopProxyHandler implements InvocationHandler {

    // This is the “real” object that we wrapped (e.g., the OrderServiceImpl instance)
    private final Object target;

    public AopProxyHandler(Object target) {
        this.target = target;
    }

    /**
     * The JVM calls this method EVERY TIME a method is called on the proxy.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
        // 1. We need to check whether our annotation is included in the ORIGINAL class's method
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        boolean isTransactional = targetMethod.isAnnotationPresent(MyTransactional.class);

        // 2. If it's present, we open a transaction (AOP “Before” advice)
        if (isTransactional) {
            System.out.println("\n[AOP-PROXY] >>> BEGIN TRANSACTION for method: " + method.getName());
        }

        Object result;
        try {
            // 3. INVOKE THE ORIGINAL BUSINESS LOGIC (using Reflection on the target object)
            result = method.invoke(target, args);
            
            // 4. If everything went correctly, we commit (AOP “AfterReturning” advice)
            if (isTransactional) {
                System.out.println("[AOP-PROXY] <<< COMMIT TRANSACTION for method: " + method.getName());
            }
        } catch (Exception e) {
            // 5. If an error occurred, we roll back the transaction (AOP “AfterThrowing” advice)
            if (isTransactional) {
                System.err.println("[AOP-PROXY] !!! ROLLBACK TRANSACTION due to error.");
            }
            throw e; // We propagate the error
        }

        return result;
    }
}