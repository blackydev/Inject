package com.patryklikus.winter.beans;

import com.patryklikus.winter.beans.Bean.Bean;
import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;
import com.patryklikus.winter.utils.searcher.ClassSearchException;
import com.patryklikus.winter.utils.searcher.ClassSearcher;
import com.patryklikus.winter.utils.searcher.ClassSearcherImpl;
import dev.mccue.guava.reflect.TypeToken;

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

        beanWrappers = null;
        packageName = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Bean<T> getBean(String name, Class<T> classType) {
        BeanKey key = new BeanKey(name, classType);
        return (Bean<T>) beans.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Bean<T> getBean(String name, TypeToken<T> classType) {
        System.out.println(classType);
        BeanKey key = new BeanKey(name, classType.getRawType());
        return (Bean<T>) beans.get(key); // todo List<String> can return List<Integer> and won't throw exception if we won't use it
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
        BeanKey key = new BeanKey(method.getName(), method.getReturnType());
        if (beans.containsKey(key)) {
            throw new BeanInitializationException(
                    "More than one bean method has the same name and returned classType: " + key);
        }
        Parameter[] params = method.getParameters();
        Object[] existedArgs = Arrays.stream(params)
                .map(param -> new BeanKey(param.getName(), param.getType()))
                .map(beans::get)
                .filter(Objects::nonNull)
                .map(Bean::value)
                .toArray();
        if (params.length != existedArgs.length) {
            return false;
        }
        Bean<?> bean = invokeBean(method, existedArgs);
        beans.put(key, bean);
        return true;
    }

    private Bean<?> invokeBean(Method method, Object[] args) {
        Object config = beanWrappers.get(method.getDeclaringClass());
        Object beanValue;
        try {
            beanValue = method.invoke(config, args);
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
        return new Bean<>(beanValue, init, run, close);
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
