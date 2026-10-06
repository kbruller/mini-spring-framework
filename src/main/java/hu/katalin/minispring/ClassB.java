package hu.katalin.minispring;

// @MyComponent // Uncomment to test CircularDependencyException
public class ClassB {
    private final ClassA classA;

    @MyAutowired
    public ClassB(ClassA classA) {
        this.classA = classA;
    }
}