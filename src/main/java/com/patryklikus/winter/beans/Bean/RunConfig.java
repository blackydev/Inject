package com.patryklikus.winter.beans.Bean;

import com.patryklikus.winter.lifecycle.Run;

import java.util.concurrent.TimeUnit;

public class RunConfig {
    private final boolean enabled;
    private final int corePoolSize;
    private final long delay;
    private final long repetitionPeriod;
    private final TimeUnit timeUnit;

    RunConfig(Run run) {
        if (run == null) {
            enabled = true;
            corePoolSize = 1;
            delay = 0;
            repetitionPeriod = 0;
            timeUnit = TimeUnit.SECONDS;
        } else {
            enabled = run.enabled();
            corePoolSize = run.corePoolSize();
            delay = run.delay();
            repetitionPeriod = run.repetitionPeriod();
            timeUnit = run.timeUnit();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int corePoolSize() {
        return corePoolSize;
    }

    public long delay() {
        return delay;
    }

    public long repetitionPeriod() {
        return repetitionPeriod;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }
}
