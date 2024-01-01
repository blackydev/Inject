package com.patryklikus.winter.utils.searcher;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toCollection;

public enum ClassSearcherImpl implements ClassSearcher {
    INSTANCE;
    private static final String ERROR_MESSAGE = "Unexpected error occurs when try to find all classes inside package";

    @Override
    public Set<Class<?>> getClassesRecursively(String packageName, Class<? extends Annotation> annotation) throws ClassSearchException {
        return getDirectory(packageName)
                .map(dir -> findClasses(new HashSet<>(), dir, packageName))
                .orElse(emptySet()).stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toCollection(HashSet::new));
    }

    /**
     * @return Physical directory where package is stored
     */
    private static Optional<File> getDirectory(String packageName) {
        String path = packageName.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(path);
        return Optional.ofNullable(resource)
                .map(URL::getFile)
                .map(File::new)
                .filter(File::isDirectory);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirectories.
     *
     * @param storage     Set where classes will be added
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return storage
     */
    private Set<Class<?>> findClasses(Set<Class<?>> storage, File directory, String packageName) throws ClassSearchException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new ClassSearchException(ERROR_MESSAGE);
        }
        for (File file : files) {
            if (file.isDirectory())
                findClasses(storage, file, packageName + "." + file.getName());
            else
                storage.add(getClass(packageName, file));
        }
        return storage;
    }

    /**
     * Gets class which represents provided file.
     */
    private Class<?> getClass(String packageName, File file) throws ClassSearchException {
        String className = file.getName().replace(".class", "");
        String classWithPackage = packageName + '.' + className;
        try {
            return Class.forName(classWithPackage);
        } catch (ClassNotFoundException e) {
            throw new ClassSearchException(ERROR_MESSAGE, e);
        }
    }
}
