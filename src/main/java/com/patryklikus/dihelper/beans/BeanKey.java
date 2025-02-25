/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.beans;

import java.lang.reflect.Type;
import java.util.Objects;

public record BeanKey(
        String name, Type classType
) {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BeanKey beanKey = (BeanKey) o;

        if (!Objects.equals(name, beanKey.name))
            return false;
        return Objects.equals(classType, beanKey.classType);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (classType != null ? classType.hashCode() : 0);
        return result;
    }
}
