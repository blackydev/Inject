/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.beans.exampleProject.models;

import java.util.Objects;

public class Color {
    String value;

    public Color(String value) {
        this.value = value;
    }

    public String getAsText() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Color color = (Color) o;
        return Objects.equals(value, color.value);
    }
}
