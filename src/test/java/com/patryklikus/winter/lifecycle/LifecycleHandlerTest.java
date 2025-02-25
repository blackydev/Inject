/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.lifecycle;

import com.patryklikus.winter.beans.Bean;
import com.patryklikus.winter.lifecycle.config.CloseConfig;
import com.patryklikus.winter.lifecycle.config.InitConfig;
import com.patryklikus.winter.lifecycle.config.RunConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LifecycleHandlerTest {
    private static final int TEST_BEANS_CAPACITY = 100;
    @Mock
    private Lifeable mockedLifeable;
    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    private LifecycleHandler lifecycleHandler;

    @RepeatedTest(10)
    @DisplayName("Should init objects in correct order")
    void initTest() {
        List<Bean<?>> beans = createBeans(true, false, false);
        var notSorted = toNotSorted(beans);
        lifecycleHandler = new LifecycleHandler(notSorted);

        lifecycleHandler.init();

        InOrder inOrder = inOrder(beans.stream().map(Bean::value).toArray());
        for (var mock : beans) {
            inOrder.verify((Initable) mock.value()).init();
        }
    }

    @RepeatedTest(10)
    @DisplayName("Should run objects")
    void runTest() {
        var executor = mockStatic(Executors.class);
        var beans = createBeans(false, true, false);
        beans.stream().map(Bean::runConfig).map(RunConfig::getDelay).forEach(System.out::println);
        var notSorted = toNotSorted(beans);
        lifecycleHandler = new LifecycleHandler(notSorted);
        executor.when(() -> Executors.newScheduledThreadPool(1)).thenReturn(scheduledExecutorService);

        lifecycleHandler.init();

        InOrder inOrder = inOrder(scheduledExecutorService);
        for (var mock : beans) {
            inOrder.verify(scheduledExecutorService).schedule((Runnable) mock.value(), 0, TimeUnit.SECONDS);
        }
        executor.close();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should schedule object run")
    void runExecutorServicesTest(boolean withFixedDelay) {
        short order = 0;
        int corePoolSize = 2, delay = 3;
        int repetitionPeriod = (withFixedDelay) ? 4 : 0;
        TimeUnit timeUnit = TimeUnit.MINUTES;

        var executor = mockStatic(Executors.class);
        executor.when(() -> Executors.newScheduledThreadPool(corePoolSize)).thenReturn(scheduledExecutorService);

        var initConfig = new InitConfig(false, order);
        var runConfig = new RunConfig(true, order, corePoolSize, delay, repetitionPeriod, timeUnit);
        var closeConfig = new CloseConfig(false, order);
        var bean = new Bean<>(mockedLifeable, initConfig, runConfig, closeConfig);

        lifecycleHandler = new LifecycleHandler(singletonList(bean));

        lifecycleHandler.init();

        if (withFixedDelay)
            verify(scheduledExecutorService).scheduleWithFixedDelay(mockedLifeable, delay, repetitionPeriod, timeUnit);
        else
            verify(scheduledExecutorService).schedule(mockedLifeable, delay, timeUnit);
        executor.close();
    }

    @Test
    @DisplayName("Should close objects in correct order")
    void closeTest() throws IOException {
        var mockedBeans = createBeans(false, false, true);
        lifecycleHandler = new LifecycleHandler(mockedBeans);
        lifecycleHandler.init();
        for (var mock : mockedBeans) {
            verify((Closeable) mock.value(), never()).close();
        }

        lifecycleHandler.close();

        InOrder inOrder = inOrder(mockedBeans.stream().map(Bean::value).toArray());
        for (var mock : mockedBeans) {
            inOrder.verify((Closeable) mock.value()).close();
        }
    }

    private List<Bean<?>> createBeans(boolean initable, boolean runnable, boolean closeable) {
        List<Bean<?>> beans = new ArrayList<>(TEST_BEANS_CAPACITY);
        for (int i = 0; i < TEST_BEANS_CAPACITY; i++) {
            short order = (short) (i - TEST_BEANS_CAPACITY / 2);

            var initConfig = new InitConfig(initable, order);
            var runConfig = new RunConfig(runnable, order, 1, 0, 0, TimeUnit.SECONDS);
            var closeConfig = new CloseConfig(closeable, order);
            var bean = new Bean<>(mock(Lifeable.class), initConfig, runConfig, closeConfig);

            beans.add(bean);
        }
        return beans;
    }

    /**
     * Introduces randomness. Test which use it should be annotated with {@link ParameterizedTest}
     */
    private List<Bean<?>> toNotSorted(List<Bean<?>> beans) {
        List<Bean<?>> notSorted = new ArrayList<>(beans);
        var random = new Random();
        for (int i = beans.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            var cache = notSorted.get(i);
            notSorted.set(i, notSorted.get(j));
            notSorted.set(j, cache);
        }
        return notSorted;
    }

    private static class Lifeable implements Initable, Runnable, Closeable {
        public void init() {
        }

        public void run() {
        }

        public void close() {
        }
    }
}
