package hu.katalin.minispring;

/**
 * Thrown when the DI container detects a circular dependency between beans.
 */
public class CircularDependencyException extends RuntimeException {
    
    public CircularDependencyException(String message) {
        super(message);
    }
}