package lifecycle;

import com.patryklikus.winter.injection.Bean.Bean;
import com.patryklikus.winter.injection.Bean.CloseConfig;
import com.patryklikus.winter.injection.Bean.InitConfig;
import com.patryklikus.winter.injection.Bean.RunConfig;
import com.patryklikus.winter.lifecycle.Initable;
import com.patryklikus.winter.lifecycle.LifecycleHandler;
import com.patryklikus.winter.lifecycle.LifecycleHandlerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

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
        List<Bean<?>> mockedBeans = mockBeans(true, false, false);
        List<Bean<?>> notSorted = toNotSorted(mockedBeans);
        lifecycleHandler = new LifecycleHandlerImpl(notSorted);

        lifecycleHandler.init();

        for (var mock : mockedBeans) {
            verify((Initable) mock.value()).init();
        }
        // todo check order
    }

    @Test
    @DisplayName("Should run objects")
    void runTest() {
        List<Bean<?>> mockedBeans = mockBeans(false, true, false);
        List<Bean<?>> notSorted = toNotSorted(mockedBeans);
        lifecycleHandler = new LifecycleHandlerImpl(notSorted);
        try (MockedStatic<Executors> executor = mockStatic(Executors.class)) {
            var executorService = mock(ScheduledExecutorService.class);
            executor.when(() -> Executors.newScheduledThreadPool(1)).thenReturn(executorService);

            lifecycleHandler.init();

            for (var mock : mockedBeans) {
                verify(executorService).schedule((Runnable) mock.value(), 0, TimeUnit.SECONDS);
            }
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should schedule object in proper way")
    void runExecutorServicesTest(boolean withFixedDelay) {
        var bean = (withFixedDelay)
                ? createRunnableMock(2, 3, 4, TimeUnit.MINUTES)
                : createRunnableMock(2, 3, 0, TimeUnit.MINUTES);

        lifecycleHandler = new LifecycleHandlerImpl(List.of(bean));
        try (MockedStatic<Executors> executor = mockStatic(Executors.class)) {
            var executorService = mock(ScheduledExecutorService.class);
            executor.when(() -> Executors.newScheduledThreadPool(2)).thenReturn(executorService);

            lifecycleHandler.init();
            if (withFixedDelay)
                verify(executorService).scheduleWithFixedDelay((Runnable) bean.value(), 3, 4, TimeUnit.MINUTES);
            else
                verify(executorService).schedule((Runnable) bean.value(), 3, TimeUnit.MINUTES);
        }
    }

    @Test
    @DisplayName("Should close objects in correct order")
    void closeTest() throws IOException {
        List<Bean<?>> mockedBeans = mockBeans(false, false, true);
        List<Bean<?>> notSorted = toNotSorted(mockedBeans);
        lifecycleHandler = new LifecycleHandlerImpl(notSorted);
        lifecycleHandler.init();

        for (var mock : mockedBeans) {
            verify((Closeable) mock.value(), never()).close();
        }

        lifecycleHandler.close();

        for (var mock : mockedBeans) {
            verify((Closeable) mock.value()).close();
        }
        // todo check order
    }

    private Bean<?> createRunnableMock(int corePoolSize, long delay, long repetitionPeriod, TimeUnit timeUnit) {
        var bean = mock(Bean.class);
        var template = mock(TestTemplate.class);

        when(bean.value()).thenReturn(template);
        when(bean.initConfig()).thenReturn(new InitConfig(false, (short) 0));
        when(bean.closeConfig()).thenReturn(new CloseConfig(false, (short) (0)));

        var runConfig = new RunConfig(true, corePoolSize, delay, repetitionPeriod, timeUnit);
        when(bean.runConfig()).thenReturn(runConfig);

        return bean;
    }

    private List<Bean<?>> mockBeans(boolean initable, boolean runnable, boolean closeable) {
        List<Bean<?>> mocks = new ArrayList<>(TEST_BEANS_CAPACITY);
        for (int i = 0; i < TEST_BEANS_CAPACITY; i++) {
            var template = mock(TestTemplate.class);
            var bean = mock(Bean.class);
            when(bean.value()).thenReturn(template);

            var initConfig = new InitConfig(initable, (short) (i / 4 - TEST_BEANS_CAPACITY / 8));
            when(bean.initConfig()).thenReturn(initConfig);

            var runConfig = new RunConfig(runnable, 1, 0, 0, TimeUnit.SECONDS);
            when(bean.runConfig()).thenReturn(runConfig);

            var closeConfig = new CloseConfig(closeable, (short) (i / 4 - TEST_BEANS_CAPACITY / 8));
            when(bean.closeConfig()).thenReturn(closeConfig);
            mocks.add(bean);
        }
        return mocks;
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
