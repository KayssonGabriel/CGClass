package org.gear.framework.core.design_patterns;

import org.gear.framework.core.design_patterns.exception.SingletonDeclarationClassViolationException;
import java.lang.reflect.InvocationTargetException;

public class Singleton<T> {
    T instance;

    public Singleton(T instance) {
        this.instance = instance;
    }

    public Singleton(Class<T> instance) {
        if(instance.getDeclaredConstructors().length > 1) {
            throw new SingletonDeclarationClassViolationException("One constructor without arguments expected.");
        }

        try {
            this.instance = instance.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public T get() {
        return this.instance;
    }
}
