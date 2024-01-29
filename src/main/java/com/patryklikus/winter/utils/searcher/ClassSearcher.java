package com.patryklikus.winter.utils.searcher;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassSearcher {
    /**
     * Gets all classes from package and sub-packages recursively. Filters packages which does not have passed annotation.
     *
     * @param packageName where searching starts
     * @param annotation  which classes must have
     * @return classes with provided annotation inside package and all sub-packages
     */
    Set<Class<?>> getClassesRecursively(String packageName, Class<? extends Annotation> annotation) throws ClassSearchException;
}
