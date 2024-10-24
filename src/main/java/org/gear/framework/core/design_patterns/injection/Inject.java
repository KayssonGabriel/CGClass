package org.gear.framework.core.design_patterns.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to inject dependencies into fields or methods.
 * The annotated field or method will be automatically populated with an instance
 * of the required dependency.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
