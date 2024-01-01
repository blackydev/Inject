package com.patryklikus.winter.inject;

import com.patryklikus.winter.utils.searcher.ClassSearchException;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.stream.Collectors.toCollection;

public class InjectProviderImpl implements InjectProvider {
    private final ClassSearcher classSearcher;
    private final Map<Class<?>, Object> configObjects;
    private final Map<InjectKey, Object> injects;
    private final String packageName;

    public InjectProviderImpl(Class<?> Outermost) {
        classSearcher = ClassSearcherImpl.INSTANCE;
        configObjects = new HashMap<>();
        injects = new HashMap<>();
        packageName = Outermost.getPackageName();
    }

    @Override
    public void init() {
        try {
            classSearcher.getClassesRecursively(packageName, Config.class).stream()
                    .peek(this::validateConfigClass)
                    .map(this::createConfigObject)
                    .forEach(o -> configObjects.put(o.getClass(), o));
        } catch (ClassSearchException e) {
            throw new InjectInitializationException(e);
        }
        extractMethods(getMethodsSortedByParameterCount());
        configObjects.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInject(String name, Class<T> classType) {
        InjectKey key = new InjectKey(name, classType);
        return (T) injects.get(key);
    }

    private List<Method> getMethodsSortedByParameterCount() {
        return configObjects.keySet().stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.getModifiers() == PUBLIC)
                .sorted(Comparator.comparingInt(Method::getParameterCount))
                .collect(toCollection(LinkedList::new));
    }

    private void extractMethods(List<Method> methods) {
        int toExtractCounter = 0;
        while (!methods.isEmpty()) {
            if (toExtractCounter == methods.size()) {
                String errorMessage = String.format("Invalid configuration. Can not find all required parameters to init %s inject.", methods.get(0));
                throw new InjectInitializationException(errorMessage);
            }
            toExtractCounter = methods.size();
            methods.removeIf(this::tryToExtractMethod);
        }
    }

    private boolean tryToExtractMethod(Method method) {
        InjectKey key = new InjectKey(method.getName(), method.getReturnType());
        if (injects.containsKey(key)) {
            throw new InjectInitializationException("More than one inject method has the same name and returned classType: " + key);
        }
        Parameter[] params = method.getParameters();
        Object[] foundInjects = Arrays.stream(params)
                .map(param -> new InjectKey(param.getName(), param.getType()))
                .map(injects::get)
                .toArray();
        if (params.length != foundInjects.length) {
            return false;
        }
        Object config = configObjects.get(method.getDeclaringClass());
        try {
            Object inject = method.invoke(config, foundInjects);
            injects.put(key, inject);
            return true;
        } catch (Exception e) {
            String message = String.format("Error when trying to use configuration method: %s#%s", config.getClass(), method.getName());
            throw new InjectInitializationException(message, e);
        }
    }

    private void validateConfigClass(Class<?> clazz) {
        if(clazz.getSuperclass() != Object.class)
            throw new InjectInitializationException("Config class can inherit just from 'Object' class");
    }

    private Object createConfigObject(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InjectInitializationException("Error during create configuration class object", e);
        }
    }
}
