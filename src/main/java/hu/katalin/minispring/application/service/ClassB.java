package hu.katalin.minispring.application.service;

import hu.katalin.minispring.framework.annotation.MyAutowired;

// @MyComponent // Uncomment to test CircularDependencyException
public class ClassB {
    private final ClassA classA;

    @MyAutowired
    public ClassB(ClassA classA) {
        this.classA = classA;
    }
}