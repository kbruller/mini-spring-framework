package hu.katalin.minispring;

@MyComponent
public class ClassA {
    private final ClassB classB;

    @MyAutowired
    public ClassA(ClassB classB) {
        this.classB = classB;
    }
}