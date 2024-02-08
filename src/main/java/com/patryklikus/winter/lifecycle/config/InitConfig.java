/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle.config;

import com.patryklikus.winter.lifecycle.Init;
import lombok.Getter;

@Getter
public class InitConfig {
    private final boolean enabled;
    private final short order;

    public InitConfig(Init init) {
        if (init == null) {
            enabled = true;
            order = 0;
        } else {
            enabled = init.enabled();
            order = init.order();
        }
    }

    public InitConfig(boolean enabled, short order) {
        this.enabled = enabled;
        this.order = order;
    }
}
