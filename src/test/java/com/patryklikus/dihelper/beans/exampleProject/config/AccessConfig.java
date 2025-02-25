/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.beans.exampleProject.config;

import com.patryklikus.dihelper.beans.Beans;
import com.patryklikus.dihelper.beans.exampleProject.models.Apple;
import com.patryklikus.dihelper.beans.exampleProject.models.Fruit;

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