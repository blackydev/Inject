package com.patryklikus.winter.beans.Bean;

import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;

public class Bean<T> {
    private final T value;
    private final InitConfig initConfig;
    private final RunConfig runConfig;
    private final CloseConfig closeConfig;

    public Bean(T value, Init init, Run run, Close close) {
        this.value = value;
        initConfig = new InitConfig(init);
        runConfig = new RunConfig(run);
        closeConfig = new CloseConfig(close);
    }


    public T value() {
        return value;
    }

    public InitConfig initConfig() {
        return initConfig;
    }

    public RunConfig runConfig() {
        return runConfig;
    }

    public CloseConfig closeConfig() {
        return closeConfig;
    }
}

