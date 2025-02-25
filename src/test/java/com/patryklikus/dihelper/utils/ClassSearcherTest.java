/* Copyright patryklikus.com All Rights Reserved. */
package com.patryklikus.dihelper.utils;

import com.patryklikus.dihelper.beans.Beans;
import com.patryklikus.dihelper.beans.exampleProject.Main;
import com.patryklikus.dihelper.beans.exampleProject.config.AccessConfig;
import com.patryklikus.dihelper.beans.exampleProject.config.Config;
import com.patryklikus.dihelper.searcher.ClassSearcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassSearcherTest {
    private static final ClassSearcher classSearcher = new ClassSearcher();

    @Test
    @DisplayName("Should return annotated classes in package and sub-packages")
    void getClassesRecursivelyTest() {
        var configSet = classSearcher.getClassesRecursively(Main.class.getPackageName(), Beans.class);

        var expected = Set.of(Config.class, AccessConfig.class);

        assertEquals(expected, configSet);
    }
}
