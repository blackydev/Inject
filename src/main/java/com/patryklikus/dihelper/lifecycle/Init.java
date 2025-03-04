/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.lifecycle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface Init {
    boolean enabled() default true;

    short order() default 0;
}
