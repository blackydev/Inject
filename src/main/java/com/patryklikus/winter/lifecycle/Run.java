/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle;

import static java.lang.annotation.ElementType.METHOD;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface Run {
    boolean enabled() default true;

    short order() default 0;

    int corePoolSize() default 1;

    long delay() default 0;

    long repetitionPeriod() default 0;

    TimeUnit timeUnit() default SECONDS;
}
