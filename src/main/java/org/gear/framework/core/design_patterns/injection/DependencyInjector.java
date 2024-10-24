package org.gear.framework.core.design_patterns.injection;

import org.gear.framework.core.log.Logger;

import java.lang.reflect.Field;

public class DependencyInjector extends Logger {

    public void resolveDependencies(Object target) {
        Class<?> componentClass = target.getClass();
        Field[] fields = componentClass.getDeclaredFields();

        for(Field field : fields) {
            if(isValidField(field)) {
                resolveDependency(field, target);
            }
        }
    }

    private void resolveDependency(Field field, Object target) {
        try {
            field.setAccessible(true);
            field.set(target, DependencyManager.getDependencyInstance(field.getType()));
        } catch (IllegalAccessException e) {
            critical("", new RuntimeException(e));
        }
    }

    private boolean isValidField(Field field) {
        if (!field.getType().isAssignableFrom(field.getType())) {
            critical("The field '" + field.getName() + "' is invalid for injection because '" + field.getType() +
                    "' is not injectable type. The injectable types must concrete classes.");
        }

        return field.isAnnotationPresent(Inject.class);
    }
}
