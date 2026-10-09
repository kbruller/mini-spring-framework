package hu.katalin.minispring.framework.core;

/**
 * Thrown when the DI container detects a circular dependency between beans.
 */
public class CircularDependencyException extends RuntimeException {
    
    public CircularDependencyException(String message) {
        super(message);
    }
}