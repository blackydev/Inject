/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle;

import com.patryklikus.winter.beans.Bean;
import com.patryklikus.winter.lifecycle.config.RunConfig;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LifecycleHandler implements Initable, Closeable {
    private final List<Closeable> closeables;
    private List<Bean<?>> beans;

    LifecycleHandler(List<Bean<?>> beans) {
        closeables = new LinkedList<>();
        this.beans = beans;
    }

    @Override
    public void init() {
        initBeans();
        runBeans();
        setCloseableBeans();
        beans = null;
    }

    @Override
    public void close() {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.err.printf("Can't close %s object", closeable);
            }
        }
    }

    private void initBeans() {
        beans.stream()
                .filter(bean -> bean.initConfig().isEnabled())
                .sorted(this::compareInitOrder)
                .map(Bean::value)
                .forEach(value -> {
                    if (value instanceof Initable initable)
                        initable.init();
                });
    }

    private void runBeans() {
        beans.stream()
                .filter(bean -> bean.runConfig().isEnabled())
                .sorted(this::compareRunOrder)
                .forEach(this::scheduleTask);
    }

    private void setCloseableBeans() {
        beans.stream()
                .filter(bean -> bean.closeConfig().isEnabled())
                .sorted(this::compareClosePriority)
                .map(Bean::value)
                .filter(Closeable.class::isInstance)
                .map(c -> (Closeable) c)
                .forEach(closeables::add);
    }

    private int compareInitOrder(Bean<?> i1, Bean<?> i2) {
        return Short.compare(i1.initConfig().getOrder(), i2.initConfig().getOrder());
    }

    private int compareRunOrder(Bean<?> i1, Bean<?> i2) {
        return Short.compare(i1.runConfig().getOrder(), i2.runConfig().getOrder());
    }

    private int compareClosePriority(Bean<?> i1, Bean<?> i2) {
        return Short.compare(i1.closeConfig().getOrder(), i2.closeConfig().getOrder());
    }

    private void scheduleTask(Bean<?> bean) {
        if (bean.value() instanceof Runnable runnable) {
            RunConfig config = bean.runConfig();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(config.getCorePoolSize());
            if (config.getRepetitionPeriod() == 0)
                executor.schedule(runnable, config.getDelay(), config.getTimeUnit());
            else
                executor.scheduleWithFixedDelay(runnable, config.getDelay(), config.getRepetitionPeriod(), config.getTimeUnit());
        }
    }
}
