package com.chundengtai.base.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring Context 工具类
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static <T> List<T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type).entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    // 上面的例子用到了这个
    public static List<Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(annotationType);

        // java 8 的写法，将 map 的 value 收集起来到一个 list 中
        return beansWithAnnotation.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());

        // java 7
//        List<Object> result = new ArrayList<>();
//        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
//            result.add(entry.getValue());
//        }
//        return result;
    }
}