/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.beans;

import com.patryklikus.dihelper.lifecycle.Close;
import com.patryklikus.dihelper.lifecycle.Init;
import com.patryklikus.dihelper.lifecycle.Run;
import com.patryklikus.dihelper.lifecycle.config.CloseConfig;
import com.patryklikus.dihelper.lifecycle.config.InitConfig;
import com.patryklikus.dihelper.lifecycle.config.RunConfig;

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
