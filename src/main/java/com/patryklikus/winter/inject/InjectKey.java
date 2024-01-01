package com.patryklikus.winter.inject;

import java.util.Objects;

public record InjectKey(
        String name, Class<?> classType
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InjectKey injectKey = (InjectKey) o;
        return Objects.equals(name, injectKey.name) && Objects.equals(classType, injectKey.classType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, classType);
    }
}
