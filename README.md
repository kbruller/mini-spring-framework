# Mini-Spring Framework: Custom IoC & AOP Container

A lightweight, custom-built Inversion of Control (IoC) container and Aspect-Oriented Programming (AOP) engine written in pure Java from scratch.

This project was built as an engineering challenge to reverse-engineer and deeply understand the "magic" behind enterprise frameworks like Spring. It intentionally avoids any external dependencies to focus purely on core Computer Science concepts, Java Reflection, and Metaprogramming.

## Key Features & Engineering Concepts

### 1. Classpath Scanning & Component Registration
*   **Concept:** Traverses the package structure to find classes annotated with `@MyComponent`.
*   **Implementation:** Implements a recursive Depth-First Search (DFS) algorithm to scan the file system dynamically using the Java `ClassLoader`.

### 2. Dependency Injection (DI) Engine
*   **Concept:** Automatically resolves and injects dependencies via constructors annotated with `@MyAutowired`.
*   **Implementation:**
    *   Uses a `ConcurrentHashMap` as the central Bean Registry (Singleton scope).
    *   Employs a recursive dependency resolution algorithm (Topological sort concept).
    *   Supports polymorphism (fetching an implementation by requesting its Interface).

### 3. Circular Dependency Detection (Fail-Fast)
*   **Concept:** Prevents `StackOverflowError` when two beans depend on each other (e.g., A -> B -> A).
*   **Implementation:** Maintains a stateful "in-progress" Set during the recursive graph traversal. If a node is visited twice in the same resolution chain, it gracefully throws a custom `CircularDependencyException`.

### 4. Aspect-Oriented Programming (AOP)
*   **Concept:** Separates Cross-Cutting Concerns (like Transaction Management) from business logic using the `@MyTransactional` annotation.
*   **Implementation:** Utilizes **JDK Dynamic Proxies** (`java.lang.reflect.Proxy`) and the `InvocationHandler` interface to wrap target beans at runtime. It intercepts method calls to dynamically add `BEGIN TRANSACTION` and `COMMIT/ROLLBACK` logic.

## Technology Stack
*   **Language:** Java 21 LTS
*   **Build Tool:** Maven
*   **Dependencies:** None (Pure Java SE, utilizing the `java.lang.reflect` package)

## How to Run
1. Clone the repository.
2. Ensure you have Java 21+ installed.
3. Run the `hu.katalin.minispring.Application` main class.
4. Observe the console output to see the IoC container booting up, resolving dependencies, generating proxies, and executing business logic with intercepted transaction logs.