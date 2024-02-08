/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans.exampleProject.config;

import com.patryklikus.winter.beans.Beans;
import com.patryklikus.winter.beans.exampleProject.models.Apple;
import com.patryklikus.winter.beans.exampleProject.models.Fruit;

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