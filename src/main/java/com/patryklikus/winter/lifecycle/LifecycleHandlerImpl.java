package com.patryklikus.winter.lifecycle;

import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.beans.Bean.RunConfig;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class LifecycleHandlerImpl implements LifecycleHandler {
    private final List<Closeable> closeables;
    private List<Bean<?>> beans;

    public LifecycleHandlerImpl(List<Bean<?>> beans) {
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
                // todo add logger
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
        beans.stream().filter(bean -> bean.runConfig().isEnabled()).forEach(this::scheduleTask);
    }

    private void setCloseableBeans() {
        beans.stream()
                .filter(bean -> bean.closeConfig().isEnabled())
                .sorted(this::compareClosePriority)
                .map(Bean::value)
                .filter(v -> v instanceof Closeable)
                .map(c -> (Closeable) c)
                .forEach(closeables::add);
    }

    private int compareInitOrder(Bean<?> i1, Bean<?> i2) {
        return Short.compare(i1.initConfig().order(), i2.initConfig().order());
    }

    private int compareClosePriority(Bean<?> i1, Bean<?> i2) {
        return Short.compare(i1.closeConfig().order(), i2.closeConfig().order());
    }

    private void scheduleTask(Bean<?> bean) {
        if (bean.value() instanceof Runnable runnable) {
            RunConfig config = bean.runConfig();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(config.corePoolSize());
            if (config.repetitionPeriod() == 0)
                executor.schedule(runnable, config.delay(), config.timeUnit());
            else
                executor.scheduleWithFixedDelay(runnable, config.delay(), config.repetitionPeriod(), config.timeUnit());
        }
    }
}
