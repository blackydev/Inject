/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans;

import com.google.common.reflect.TypeToken;
import com.patryklikus.winter.lifecycle.Initable;
import java.util.Map;

public interface BeanProvider extends Initable {
    /**
     * Searches recursively all packages for classes with {@link Beans} annotation.
     * Checks that superclass is equal to {@link Object} if not throws {@link BeanInitializationException}.
     * Retrieves all public methods from these classes. Sorts methods by parameters count.
     * Creates objects using these methods and saves them.
     *
     * @throws BeanInitializationException if any {@link Beans} superclass is other than {@link Object}
     */
    void init() throws BeanInitializationException;

    Map<BeanKey, Bean<?>> getBeans();

    <T> Bean<T> getBean(String name, Class<T> type);

    <T> Bean<T> getBean(String name, TypeToken<T> type);
}
