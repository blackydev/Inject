package com.patryklikus.winter.beans.Bean;

import com.patryklikus.winter.lifecycle.Close;

public class CloseConfig {
    private final boolean enabled;
    private final short order;

    public CloseConfig(boolean enabled, short order) {
        this.enabled = enabled;
        this.order = order;
    }

    CloseConfig(Close close) {
        if (close == null) {
            enabled = true;
            order = 0;
        } else {
            enabled = close.enabled();
            order = close.order();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public short order() {
        return order;
    }
}
