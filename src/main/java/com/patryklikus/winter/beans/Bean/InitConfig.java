/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans.Bean;

import com.patryklikus.winter.lifecycle.Init;

public class InitConfig {
    private final boolean enabled;
    private final short order;

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
