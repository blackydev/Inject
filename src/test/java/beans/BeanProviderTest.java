package beans;

import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.beans.BeanProvider;
import com.patryklikus.winter.beans.BeanProviderImpl;
import dev.mccue.guava.reflect.TypeToken;
import beans.exampleProject.Main;
import beans.exampleProject.models.Color;
import beans.exampleProject.models.fruits.Apple;
import beans.exampleProject.models.fruits.Fruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanProviderTest {
    private BeanProvider beanProvider;

    @BeforeEach
    void setUp() {
        beanProvider = new BeanProviderImpl(Main.class);
        beanProvider.init();
    }

    @Test
    @DisplayName("Should contain correct number of beans in storage")
    void getBeansTest() throws NoSuchFieldException, IllegalAccessException {
        Field beansField = BeanProviderImpl.class.getDeclaredField("beans");
        beansField.setAccessible(true);

        var storage = (Map<?, ?>) beansField.get(beanProvider);

        assertEquals(7, storage.size());
    }

    @Test
    @DisplayName("Should return each bean with correct value and type")
    void getBeanTest() {
        String expectedRed = "red";
        String expectedBlue = "blue";
        List<String> expectedList = List.of("Hello", "World", "!");

        String red = beanProvider.getBean("red", String.class).value();
        Color redColor = beanProvider.getBean("redColor", Color.class).value();
        String blue = beanProvider.getBean("blue", String.class).value();
        Color blueColor = beanProvider.getBean("blueColor", Color.class).value();
        Fruit banana = beanProvider.getBean("bananaFruit", Fruit.class).value();
        Fruit apple = beanProvider.getBean("apple", Apple.class).value();
        List<Integer> texts = beanProvider.getBean("texts", new TypeToken<List<Integer>>() {
        }).value();
        assertEquals(expectedRed, red);
        assertEquals(expectedRed, redColor.getAsText());
        assertEquals(expectedBlue, blue);
        assertEquals(expectedBlue, blueColor.getAsText());
        assertNotNull(banana);
        assertNotNull(apple);
        assertNotNull(texts);
        assertEquals(expectedList, texts);
    }

    @Test
    @DisplayName("Should return beans with proper configuration")
    void getBeanLifePriorityTest() {
        Bean<Color> blueBean = beanProvider.getBean("blueColor", Color.class);
        Bean<Color> redBean = beanProvider.getBean("redColor", Color.class);

        // default config
        assertEquals(blueBean.initConfig().isEnabled(), true);
        assertEquals(blueBean.initConfig().order(), 0);

        assertEquals(blueBean.runConfig().isEnabled(), true);
        assertEquals(blueBean.runConfig().delay(), 0);
        assertEquals(blueBean.runConfig().repetitionPeriod(), 0);
        assertEquals(blueBean.runConfig().timeUnit(), SECONDS);

        assertEquals(blueBean.closeConfig().isEnabled(), true);
        assertEquals(blueBean.closeConfig().order(), 0);
        // Lifecycle annotations
        assertEquals(redBean.initConfig().isEnabled(), false);
        assertEquals(redBean.initConfig().order(), 1);

        assertEquals(redBean.runConfig().isEnabled(), false);
        assertEquals(redBean.runConfig().delay(), 0);
        assertEquals(redBean.runConfig().repetitionPeriod(), 0);
        assertEquals(redBean.runConfig().timeUnit(), SECONDS);

        assertEquals(redBean.closeConfig().isEnabled(), false);
        assertEquals(redBean.closeConfig().order(), 3);
    }
}
