package com.patryklikus.winter.injection.Bean;

import com.patryklikus.winter.lifecycle.Init;

public class InitConfig {
    private final boolean enabled;
    private final short order;

    public InitConfig(boolean enabled, short order) {
        this.enabled = enabled;
        this.order = order;
    }

    InitConfig(Init init) {
        if (init == null) {
            enabled = true;
            order = 0;
        } else {
            enabled = init.enabled();
            order = init.order();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public short order() {
        return order;
    }
}
