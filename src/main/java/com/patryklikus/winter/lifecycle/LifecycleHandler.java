package com.patryklikus.winter.lifecycle;

import java.io.Closeable;

// todo add descriptions
public interface LifecycleHandler extends Initable, Closeable {
    /**
     * Initialize {@link Initable} and run {@link Runnable} object. Saves {@link Closeable} b
     */
    @Override
    void init();

    /**
     * Closes all {@link Closeable} objects.
     */
    @Override
    void close();
}
