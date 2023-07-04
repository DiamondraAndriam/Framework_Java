package etu1748.framework.annotation;

import java.lang.annotation.ElementType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Session {
    String value() default "";
}
