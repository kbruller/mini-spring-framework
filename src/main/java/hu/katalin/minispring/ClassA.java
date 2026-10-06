package hu.katalin.minispring;

// @MyComponent // Uncomment to test CircularDependencyException
public class ClassA {
    private final ClassB classB;

    @MyAutowired
    public ClassA(ClassB classB) {
        this.classB = classB;
    }
}