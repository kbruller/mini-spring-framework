package hu.katalin.minispring;

@MyComponent
public class ClassB {
    private final ClassA classA;

    @MyAutowired
    public ClassB(ClassA classA) {
        this.classA = classA;
    }
}