package com.patryklikus.winter.beans;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * All classes with this annotation will be searched to initialize objects using all its public methods. This class can inherit just from {@link Object}.
 */
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Beans {
}
