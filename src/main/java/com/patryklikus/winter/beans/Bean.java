/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans;

import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;
import com.patryklikus.winter.lifecycle.config.CloseConfig;
import com.patryklikus.winter.lifecycle.config.InitConfig;
import com.patryklikus.winter.lifecycle.config.RunConfig;

public record Bean<T>(
        T value,
        InitConfig initConfig,
        RunConfig runConfig,
        CloseConfig closeConfig
) {
    public Bean(T value, Init init, Run run, Close close) {
        this(value, new InitConfig(init), new RunConfig(run), new CloseConfig(close));
    }
}
