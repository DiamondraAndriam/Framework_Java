package etu1748.framework.annotation;

import java.lang.annotation.ElementType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value();
}
