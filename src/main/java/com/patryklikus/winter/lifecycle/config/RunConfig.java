/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle.config;

import com.patryklikus.winter.lifecycle.Run;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

@Getter
public class RunConfig {
    private final boolean enabled;
    private final short order;
    private final int corePoolSize;
    private final long delay;
    private final long repetitionPeriod;
    private final TimeUnit timeUnit;

    public RunConfig(Run run) {
        if (run == null) {
            enabled = true;
            order = 0;
            corePoolSize = 1;
            delay = 0;
            repetitionPeriod = 0;
            timeUnit = TimeUnit.SECONDS;
        } else {
            enabled = run.enabled();
            order = run.order();
            corePoolSize = run.corePoolSize();
            delay = run.delay();
            repetitionPeriod = run.repetitionPeriod();
            timeUnit = run.timeUnit();
        }
    }

    public RunConfig(boolean enabled, short order, int corePoolSize, long delay, long repetitionPeriod, TimeUnit timeUnit) {
        this.enabled = enabled;
        this.order = order;
        this.corePoolSize = corePoolSize;
        this.delay = delay;
        this.repetitionPeriod = repetitionPeriod;
        this.timeUnit = timeUnit;
    }
}
