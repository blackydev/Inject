package lifecycle;

import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.beans.Bean.CloseConfig;
import com.patryklikus.winter.beans.Bean.InitConfig;
import com.patryklikus.winter.beans.Bean.RunConfig;
import com.patryklikus.winter.lifecycle.Initable;
import com.patryklikus.winter.lifecycle.LifecycleHandler;
import com.patryklikus.winter.lifecycle.LifecycleHandlerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class LifecycleHandlerTest {
    private static final int TEST_BEANS_CAPACITY = 100;
    private LifecycleHandler lifecycleHandler;

    @Test
    @DisplayName("Should init objects in correct order")
    void initTest() {
        var mockedBeans = mockBeans(true, false, false);
        var notSorted = toNotSorted(mockedBeans);
        mockedBeans.stream().map(Bean::initConfig).map(InitConfig::order).forEach(System.out::println);
        lifecycleHandler = new LifecycleHandlerImpl(notSorted);

        lifecycleHandler.init();

        InOrder inOrder = inOrder(mockedBeans.stream().map(Bean::value).toArray());
        for (var mock : mockedBeans) {
            inOrder.verify((Initable) mock.value()).init();
        }
    }

    @Test
    @DisplayName("Should run objects")
    void runTest() {
        var executor = mockStatic(Executors.class);
        var mockedBeans = mockBeans(false, true, false);
        var notSorted = toNotSorted(mockedBeans);
        lifecycleHandler = new LifecycleHandlerImpl(notSorted);
        var executorService = mock(ScheduledExecutorService.class);
        executor.when(() -> Executors.newScheduledThreadPool(1)).thenReturn(executorService);

        lifecycleHandler.init();

        for (var mock : mockedBeans) {
            verify(executorService).schedule((Runnable) mock.value(), 0, TimeUnit.SECONDS);
        }
        executor.close();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should schedule object run")
    void runExecutorServicesTest(boolean withFixedDelay) {
        var executor = mockStatic(Executors.class);
        int corePoolSize = 2, delay = 3;
        int repetitionPeriod = (withFixedDelay) ? 4 : 0;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        var bean = mockRunnableBean(corePoolSize, delay, repetitionPeriod, timeUnit);
        lifecycleHandler = new LifecycleHandlerImpl(List.of(bean));
        var executorService = mock(ScheduledExecutorService.class);
        executor.when(() -> Executors.newScheduledThreadPool(corePoolSize)).thenReturn(executorService);

        lifecycleHandler.init();

        if (withFixedDelay)
            verify(executorService).scheduleWithFixedDelay((Runnable) bean.value(), delay, repetitionPeriod, timeUnit);
        else
            verify(executorService).schedule((Runnable) bean.value(), delay, timeUnit);
        executor.close();
    }

    @Test
    @DisplayName("Should close objects in correct order")
    void closeTest() throws IOException {
        var mockedBeans = mockBeans(false, false, true);
        lifecycleHandler = new LifecycleHandlerImpl(mockedBeans);
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

    private List<Bean<?>> mockBeans(boolean initable, boolean runnable, boolean closeable) {
        List<Bean<?>> mocks = new ArrayList<>(TEST_BEANS_CAPACITY);
        for (int i = 0; i < TEST_BEANS_CAPACITY; i++) {
            var bean = mock(Bean.class);
            when(bean.value()).thenReturn(mock(TestTemplate.class));

            int order = i - TEST_BEANS_CAPACITY / 2;
            mockInitConfig(bean, initable, order);
            mockRunConfig(bean, runnable, 1, 0, 0, TimeUnit.SECONDS);
            mockCloseConfig(bean, closeable, order);

            mocks.add(bean);
        }
        return mocks;
    }

    private Bean<?> mockRunnableBean(int corePoolSize, long delay, long repetitionPeriod, TimeUnit timeUnit) {
        var bean = mock(Bean.class);
        when(bean.value()).thenReturn(mock(TestTemplate.class));

        mockInitConfig(bean, false, 0);
        mockRunConfig(bean, true, corePoolSize, delay, repetitionPeriod, timeUnit);
        mockCloseConfig(bean, false, 0);

        return bean;
    }

    private void mockInitConfig(Bean<?> bean, boolean initable, int order) {
        var initConfig = mock(InitConfig.class);
        when(initConfig.isEnabled()).thenReturn(initable);
        when(initConfig.order()).thenReturn((short) order);
        when(bean.initConfig()).thenReturn(initConfig);
    }

    private void mockRunConfig(Bean<?> bean, boolean runnable, int corePoolSize, long delay, long repetitionPeriod, TimeUnit timeUnit) {
        var runConfig = mock(RunConfig.class);
        when(runConfig.isEnabled()).thenReturn(runnable);
        when(runConfig.corePoolSize()).thenReturn(corePoolSize);
        when(runConfig.delay()).thenReturn(delay);
        when(runConfig.repetitionPeriod()).thenReturn(repetitionPeriod);
        when(runConfig.timeUnit()).thenReturn(timeUnit);
        when(bean.runConfig()).thenReturn(runConfig);
    }

    private void mockCloseConfig(Bean<?> bean, boolean closeable, int order) {
        var closeConfig = mock(CloseConfig.class);
        when(closeConfig.isEnabled()).thenReturn(closeable);
        when(closeConfig.order()).thenReturn((short) order);
        when(bean.closeConfig()).thenReturn(closeConfig);
    }

    private List<Bean<?>> toNotSorted(List<Bean<?>> beans) {
        List<Bean<?>> notSorted = new ArrayList<>(beans.size());
        for (int k = 0; k < beans.size(); k++) {
            int i = k;
            if (i % 2 == 1) {
                i = beans.size() - k;
            }
            notSorted.add(beans.get(i));
        }
        return notSorted;
    }

    private static class TestTemplate implements Initable, Runnable, Closeable {
        public void init() {
        }

        public void run() {
        }

        public void close() {
        }
    }
}
