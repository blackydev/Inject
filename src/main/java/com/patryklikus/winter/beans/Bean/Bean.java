/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.beans.Bean;

import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;

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
