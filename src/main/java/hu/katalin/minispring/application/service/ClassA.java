package hu.katalin.minispring.application.service;

import hu.katalin.minispring.framework.annotation.MyAutowired;

// @MyComponent // Uncomment to test CircularDependencyException
public class ClassA {
    private final ClassB classB;

    @MyAutowired
    public ClassA(ClassB classB) {
        this.classB = classB;
    }
}