package application;

import java.lang.annotation.*;


/**
 * Class-level annotation to define selection priority for creating a
 * {@link Runnable} application instance.
 * {@code param} {@code priority} priority of {@link Runnable} class
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface RunPriority {
    /**
     * Provide Application priority value.
     * @return Application priority value
     */
    int priority() default 0;
}