package com.patryklikus.winter.beans;

import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.lifecycle.Initable;

import java.lang.reflect.Type;

public interface BeanProvider extends Initable {
    /**
     * Searches recursively all packages for classes with {@link Beans} annotation.
     * Checks that superclass is equal to {@link Object} if not throws {@link BeanInitializationException}.
     * Retrieves all methods from these classes. Filters nonpublic methods. Sorts methods by parameters count.
     * Creates objects using these methods and saves them.
     *
     * @throws BeanInitializationException if any {@link Beans} superclass is other than {@link Object}
     */
    void init() throws BeanInitializationException;

    <T> Bean<T> getBean(String name, Class<T> classType);

    <T> Bean<T> getBean(String name, Type classType);
}
