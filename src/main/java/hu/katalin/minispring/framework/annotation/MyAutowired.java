package hu.katalin.minispring.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a constructor to be autowired by the Mini-Spring's dependency injection facilities.
 */
@Target(ElementType.CONSTRUCTOR) // Ezt csak konstruktorokra tehetjük rá!
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAutowired {
}