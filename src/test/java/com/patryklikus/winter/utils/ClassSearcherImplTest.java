/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.winter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.patryklikus.winter.beans.Beans;
import com.patryklikus.winter.beans.exampleProject.Main;
import com.patryklikus.winter.beans.exampleProject.config.AccessConfig;
import com.patryklikus.winter.beans.exampleProject.config.Config;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClassSearcherImplTest {
    private static final ClassSearcher classSearcher = new ClassSearcherImpl();

    @Test
    @DisplayName("Should return annotated classes in package and sub-packages")
    void getClassesRecursivelyTest() {
        var configSet = classSearcher.getClassesRecursively(Main.class.getPackageName(), Beans.class);

        var expected = Set.of(Config.class, AccessConfig.class);

        assertEquals(expected, configSet);
    }
}
