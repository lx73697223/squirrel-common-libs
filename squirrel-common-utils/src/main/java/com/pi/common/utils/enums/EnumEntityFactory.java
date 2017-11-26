package com.pi.common.utils.enums;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

public class EnumEntityFactory {

    private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();

    private static final ConcurrentMap<Class<? extends EnumEntity<?>>, Map<Object, EnumEntity<?>>> ENUM_CACHE = new ConcurrentHashMap<>();

    public static Map<Object, EnumEntity<?>> getEnumMap(Class<? extends EnumEntity<?>> enumEntityClass) {
        return ENUM_CACHE.computeIfAbsent(enumEntityClass, clazz -> {
            EnumEntity<?>[] enumConstants = clazz.getEnumConstants();
            Map<Object, EnumEntity<?>> enumMap = Maps.newHashMapWithExpectedSize(enumConstants.length);
            for (EnumEntity<?> enumEntity : enumConstants) {
                enumMap.put(enumEntity.getValue(), enumEntity);
                if (!enumEntity.getAltLookupValue().equals(enumEntity.getValue())) {
                    enumMap.put(enumEntity.getAltLookupValue(), enumEntity);
                }
            }
            return Collections.unmodifiableMap(enumMap);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends EnumEntity<?>> T getUnchecked(Class<T> enumEntityClass, Object value) {
        if (value == null) {
            return null;
        }
        Class<?> valueType = EnumEntityUtils.getEnumEntityValueType(enumEntityClass);
        if (valueType != value.getClass()) {
            value = CONVERSION_SERVICE.convert(value, valueType);
        }
        return (T) getEnumMap(enumEntityClass).get(value);
    }

    public static <T extends EnumEntity<?>> T get(Class<T> enumEntityClass, Object value) {
        T result = getUnchecked(enumEntityClass, value);
        Objects.requireNonNull(result, () -> String.format("Can not find %s of enum %s", value, enumEntityClass));
        return result;
    }

}
