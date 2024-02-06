package com.patryklikus.winter.beans;

import com.google.common.reflect.TypeToken;
import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;
import com.patryklikus.winter.utils.searcher.ClassSearchException;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class BeanProviderImpl implements BeanProvider {
    private final Map<BeanKey, Bean<?>> beans;
    private Map<Class<?>, Object> beanWrappers;
    private String packageName;

    public BeanProviderImpl(Class<?> Outermost) {
        beanWrappers = new HashMap<>();
        beans = new HashMap<>();
        packageName = Outermost.getPackageName();
    }

    @Override
    public void init() {
        ClassSearcher classSearcher = new ClassSearcherImpl();
        try {
            classSearcher.getClassesRecursively(packageName, Beans.class).stream()
                    .peek(this::validateBeanWrapper)
                    .map(this::createBeanWrapper)
                    .forEach(wrapper -> beanWrappers.put(wrapper.getClass(), wrapper));
        } catch (ClassSearchException e) {
            throw new BeanInitializationException(e);
        }
        extractMethods(getMethodsSortedByParameterCount());

        packageName = null;
        beanWrappers = null;
    }

    @Override
    public Map<BeanKey, Bean<?>> getBeans() {
        return beans;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Bean<T> getBean(String name, Class<T> type) {
        BeanKey key = new BeanKey(name, type);
        return (Bean<T>) beans.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Bean<T> getBean(String name, TypeToken<T> type) {
        BeanKey key = new BeanKey(name, type.getType());
        return (Bean<T>) beans.get(key);
    }

    private List<Method> getMethodsSortedByParameterCount() {
        return beanWrappers.keySet().stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .sorted(Comparator.comparingInt(Method::getParameterCount))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .collect(toCollection(LinkedList::new));
    }

    private void extractMethods(List<Method> methods) {
        int toExtractCounter = 0;
        while (!methods.isEmpty()) {
            if (toExtractCounter == methods.size()) {
                String errorMessage = String.format(
                        "Invalid configuration. Can not find all required parameters to init %s bean.", methods.get(0)
                );
                throw new BeanInitializationException(errorMessage);
            }
            toExtractCounter = methods.size();
            methods.removeIf(this::tryToExtractMethod);
        }
    }

    private boolean tryToExtractMethod(Method method) {
        BeanKey key = new BeanKey(method.getName(), method.getGenericReturnType());
        if (beans.containsKey(key)) {
            throw new BeanInitializationException(
                    "More than one bean method has the same name and returned classType: " + key);
        }
        var arguments = new LinkedList<>();
        for (Parameter param : method.getParameters()) {
            BeanKey paramKey = new BeanKey(param.getName(), param.getParameterizedType());
            var bean = beans.get(paramKey);
            if (bean == null) {
                return false;
            }
            arguments.add(bean.value());
        }
        Bean<?> bean = createBean(method, arguments);
        beans.put(key, bean);
        return true;
    }

    private Bean<?> createBean(Method method, List<?> args) {
        Object instance, config = beanWrappers.get(method.getDeclaringClass());
        try {
            instance = method.invoke(config, args.toArray());
        } catch (Exception e) {
            // todo implement better errors
            String message = String.format("Error when trying to use beans method: %s#%s",
                    config.getClass(), method.getName()
            );
            throw new BeanInitializationException(message, e);
        }
        Init init = extractAnnotationSafety(method, Init.class);
        Run run = extractAnnotationSafety(method, Run.class);
        Close close = extractAnnotationSafety(method, Close.class);
        return new Bean<>(instance, init, run, close);
    }

    private <T extends Annotation> T extractAnnotationSafety(Method method, Class<T> annotationClass) {
        if (method.isAnnotationPresent(annotationClass))
            return method.getAnnotation(annotationClass);
        return null;
    }

    private void validateBeanWrapper(Class<?> clazz) {
        if (clazz.getSuperclass() != Object.class)
            throw new BeanInitializationException("Config class can inherit just from 'Object' class");
    }

    private Object createBeanWrapper(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeanInitializationException("Error during create configuration class object", e);
        }
    }
}
