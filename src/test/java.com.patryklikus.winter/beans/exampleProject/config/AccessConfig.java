/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans.exampleProject.config;

import beans.exampleProject.models.Apple;
import beans.exampleProject.models.Fruit;
import com.patryklikus.winter.beans.Beans;

@Beans
public class AccessConfig {
    public Fruit apple() {
        return new Apple();
    }

    protected Apple protectedApple() {
        return new Apple();
    }

    private Fruit privateApple() {
        return new Apple();
    }
}