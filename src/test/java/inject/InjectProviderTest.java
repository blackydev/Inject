package inject;

import com.patryklikus.winter.inject.InjectKey;
import com.patryklikus.winter.inject.InjectProvider;
import com.patryklikus.winter.inject.InjectProviderImpl;
import inject.exampleProject.Main;
import inject.exampleProject.models.Color;
import inject.exampleProject.models.config.FruitConfig;
import inject.exampleProject.models.fruits.Apple;
import inject.exampleProject.models.fruits.Banana;
import inject.exampleProject.models.fruits.Fruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InjectProviderTest {
    private InjectProvider injectProvider;

    @BeforeEach
    void setUp() {
        injectProvider = new InjectProviderImpl(Main.class);
        injectProvider.init();
    }

    @Test
    @DisplayName("Should contain all injects in storage")
    @SuppressWarnings("unchecked")
    void getInjectsTest() throws NoSuchFieldException, IllegalAccessException {
        Field injectsField = InjectProviderImpl.class.getDeclaredField("injects");
        injectsField.setAccessible(true);
        Map<InjectKey, Object> storage = (Map<InjectKey, Object>) injectsField.get(injectProvider);

        assertEquals(7, storage.size());
        InjectKey key = new InjectKey("redText", String.class);
        assertEquals("red", storage.get(key));

        key = new InjectKey("red", Color.class);
        assertEquals(new Color("red"), storage.get(key));

        key = new InjectKey("blueText", String.class);
        assertEquals("blue", storage.get(key));

        key = new InjectKey("blue", Color.class);
        assertEquals(new Color("blue"), storage.get(key));

        key = new InjectKey("bananaFruit", Fruit.class);
        assertTrue(storage.get(key) instanceof Banana);

        key = new InjectKey("apple", Apple.class);
        assertTrue(storage.get(key) instanceof Apple);

        key = new InjectKey("texts", List.class);
        assertTrue(storage.get(key) instanceof List);
    }

    @Test
    @DisplayName("Should return each inject with type")
    @SuppressWarnings("unchecked")
    void getInjectTest() {
        String text = injectProvider.getInject("redText", String.class);
        assertEquals("red", text);

        Color color = injectProvider.getInject("red", Color.class);
        assertEquals(new Color("red"), color);

        text = injectProvider.getInject("blueText", String.class);
        assertEquals("blue", text);

        color = injectProvider.getInject("blue", Color.class);
        assertEquals(new Color("blue"), color);

        Fruit banana = injectProvider.getInject("bananaFruit", Fruit.class);
        assertNotNull(banana);

        Fruit apple = injectProvider.getInject("apple", Apple.class);
        assertNotNull(apple);

        List<String> texts = injectProvider.getInject("texts", List.class);
        assertNotNull(texts);
        assertEquals(List.of("Hello", "World", "!"), texts);
    }
}
