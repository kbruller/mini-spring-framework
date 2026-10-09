package hu.katalin.minispring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that requires a transaction. 
 * The AOP engine will intercept calls to this method and wrap it in a transaction boundary.
 */
@Target(ElementType.METHOD) // Now let's put it in a METHOD!
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTransactional {
}