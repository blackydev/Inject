/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle.config;

import com.patryklikus.winter.lifecycle.Close;
import lombok.Getter;

@Getter
public class CloseConfig {
    private final boolean enabled;
    private final short order;

    public CloseConfig(Close close) {
        if (close == null) {
            enabled = true;
            order = 0;
        } else {
            enabled = close.enabled();
            order = close.order();
        }
    }

    public CloseConfig(boolean enabled, short order) {
        this.enabled = enabled;
        this.order = order;
    }
}
