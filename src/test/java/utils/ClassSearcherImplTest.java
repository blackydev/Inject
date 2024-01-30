package utils;

import beans.exampleProject.Main;
import beans.exampleProject.models.config.FruitConfig;
import beans.exampleProject.models.config.other.ColorConfig;
import com.patryklikus.winter.beans.Beans;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassSearcherImplTest {
    private static final ClassSearcher classSearcher = new ClassSearcherImpl();

    @Test
    @DisplayName("Should return annotated classes in package and sub-packages")
    void getClassesRecursivelyTest() {
        var configSet = classSearcher.getClassesRecursively(Main.class.getPackageName(), Beans.class);

        var expected = Set.of(ColorConfig.class, FruitConfig.class);

        assertEquals(expected, configSet);
    }
}
