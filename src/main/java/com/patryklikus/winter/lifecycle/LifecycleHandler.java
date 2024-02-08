/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle;

import java.io.Closeable;

/**
 * Handles all beans which implements {@link Initable}, {@link Runnable} or {@link Closeable}.
 */
public interface LifecycleHandler extends Initable, Closeable {
    /**
     * Initialize {@link Initable} objects with provided order.
     * Run {@link Runnable} objects.
     * Saves objects {@link Closeable} to be closed later.
     */
    @Override
    void init();

    /**
     * Closes all {@link Closeable} objects saved in {@link LifecycleHandler#init}.
     */
    @Override
    void close();
}
