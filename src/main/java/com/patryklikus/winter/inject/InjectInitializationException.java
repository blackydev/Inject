package com.patryklikus.winter.inject;

public class InjectInitializationException extends RuntimeException {
    public InjectInitializationException(Throwable cause) {
        super(cause);
    }

    public InjectInitializationException(String message) {
        super(message);
    }
    public InjectInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
