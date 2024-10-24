package org.gear.framework.core.design_patterns.injection;

import java.util.HashMap;
import java.util.Map;

public class DependencyManager {

    private static final Map<Class<?>, Object> dependencies = new HashMap<>();

    public static void injectDependency(Object dependency) {
        dependencies.putIfAbsent(dependency.getClass(), dependency);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDependencyInstance(Class<T> dependencyType) {
        T dependency = (T) dependencies.get(dependencyType);

        if(dependency == null) {
            throw new NullPointerException("The required depedency is not created.");
        }

        return dependency ;
    }
}
