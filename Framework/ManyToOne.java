package etu1748.framework.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    String value() default "";
}
