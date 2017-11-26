package com.pi.common.utils.enums;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import com.pi.common.utils.core.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.ClassUtils;

public final class EnumEntityUtils {

    private static final ConcurrentMap<Class<? extends EnumEntity<?>>, Class<?>> ENUM_VALUE_TYPE_CACHE = new ConcurrentHashMap<>();

    private EnumEntityUtils() {
    }

    public static Class<?> getEnumEntityValueType(Class<? extends EnumEntity<?>> enumEntityClass) {
        return ENUM_VALUE_TYPE_CACHE.computeIfAbsent(enumEntityClass, clazz -> {
            ParameterizedType interfaceType = ReflectionUtils.findSpecificGenericInterfaces(clazz);
            return (Class<?>) interfaceType.getActualTypeArguments()[0];
        });
    }

    public static void scanEnumEntityPackages(Consumer<Class<? extends EnumEntity<Object>>> consumer,
            String... enumEntityPackages) {
        if (ArrayUtils.isEmpty(enumEntityPackages)) {
            return;
        }

        ClassPathEnumEntityScanner scanner = new ClassPathEnumEntityScanner();
        for (String enumEntityPackage : enumEntityPackages) {
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(enumEntityPackage)) {
                @SuppressWarnings("unchecked")
                Class<? extends EnumEntity<Object>> type = (Class<? extends EnumEntity<Object>>) ClassUtils
                        .resolveClassName(beanDefinition.getBeanClassName(), ClassUtils.getDefaultClassLoader());
                if (!type.isAnonymousClass() && type.isEnum()) {
                    consumer.accept(type);
                }
            }
        }
    }

}
