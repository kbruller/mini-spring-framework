package hu.katalin.minispring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a component that should be managed by our custom IoC container.
 * The container will automatically detect, instantiate, and manage the lifecycle of these classes.
 */
@Target(ElementType.TYPE) // Restricts the annotation to be used on Classes only.
@Retention(RetentionPolicy.RUNTIME) // REQUIRED: Ensures the annotation is available at runtime for the Reflection API.
public @interface MyComponent {
}