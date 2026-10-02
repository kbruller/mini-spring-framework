package hu.katalin.minispring;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Simulates the @ComponentScan functionality of Spring.
 * It traverses the file system (directory tree) to find all compiled .class files
 * and checks if they are annotated with @MyComponent.
 */
public class ComponentScanner {

    /**
     * Scans a given base package for components.
     * 
     * @param basePackage e.g., "hu.katalin.minispring"
     * @return A Set of Class objects that are marked with @MyComponent
     */
    public Set<Class<?>> scan(String basePackage) {
        // “Fail-fast” null checking in pure Java
        Objects.requireNonNull(basePackage, "basePackage cannot be null!");

        Set<Class<?>> components = new HashSet<>();

        // 1. Convert package name to file system path (replace dots with slashes)
        String path = basePackage.replace('.', '/');

        try {
            // 2. Ask the ClassLoader where this path is physically located on the disk
            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
            
            if (resource == null) {
                throw new IllegalArgumentException("Package not found: " + basePackage);
            }

            File directory = new File(resource.getFile());

            // 3. Start the recursive directory traversal (DFS)
            findClasses(directory, basePackage, components);

        } catch (Exception e) {
            System.err.println("Error during component scanning: " + e.getMessage());
        }

        return components;
    }

    /**
     * Recursive helper method to traverse directories and find .class files.
     */
    private void findClasses(File directory, String packageName, Set<Class<?>> components) throws ClassNotFoundException {
        // List all files and subdirectories in the current directory
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // If it's a directory, go deeper (recursive call), append the directory name to the package
                findClasses(file, packageName + "." + file.getName(), components);
            } else if (file.getName().endsWith(".class")) {
                // If it's a compiled class file, extract the class name
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                
                // Load the class into memory (without initializing it yet)
                Class<?> clazz = Class.forName(className);

                // Check for our custom annotation
                if (clazz.isAnnotationPresent(MyComponent.class)) {
                    components.add(clazz);
                }
            }
        }
    }
}