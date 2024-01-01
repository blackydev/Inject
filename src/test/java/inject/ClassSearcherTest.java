package inject;

import com.patryklikus.winter.inject.Config;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;
import inject.exampleProject.Main;
import inject.exampleProject.models.config.FruitConfig;
import inject.exampleProject.models.config.other.ColorConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassSearcherTest {
    private final ClassSearcher classSearcher = ClassSearcherImpl.INSTANCE;

    @Test
    @DisplayName("Should return annotated classes in package and sub-packages")
    void getClassesRecursivelyTest() {
        var configSet = classSearcher.getClassesRecursively(Main.class.getPackageName(), Config.class);

        Set<Class<?>> expected = Set.of(ColorConfig.class, FruitConfig.class);

        assertEquals(expected, configSet);
    }
}
