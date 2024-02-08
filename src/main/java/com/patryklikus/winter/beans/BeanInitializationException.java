/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans;

public class BeanInitializationException extends RuntimeException {
    public BeanInitializationException(Throwable cause) {
        super(cause);
    }

    public BeanInitializationException(String message) {
        super(message);
    }

    public BeanInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
