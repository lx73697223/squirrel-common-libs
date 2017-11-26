package com.pi.common.utils.enums;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class NamedEnumEntityFactory {

    private static final ConcurrentMap<Class<? extends NamedEnumEntity<?>>, Map<String, NamedEnumEntity<?>>> ENUM_CACHE = new ConcurrentHashMap<>();

    public static Map<String, NamedEnumEntity<?>> getEnumMap(Class<? extends NamedEnumEntity<?>> enumEntityClass) {
        return ENUM_CACHE.computeIfAbsent(enumEntityClass, clazz -> {
            NamedEnumEntity<?>[] enumConstants = clazz.getEnumConstants();
            Map<String, NamedEnumEntity<?>> nameEnumMap = new HashMap<>();
            for (NamedEnumEntity<?> enumEntity : enumConstants) {
                nameEnumMap.put(enumEntity.getName(), enumEntity);
            }
            return nameEnumMap;
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends NamedEnumEntity<?>> T getUnchecked(Class<T> enumEntityClass, String value) {
        return (T) getEnumMap(enumEntityClass).get(value);
    }

    public static <T extends NamedEnumEntity<?>> T get(Class<T> enumEntityClass, String value) {
        T result = getUnchecked(enumEntityClass, value);
        Preconditions.checkNotNull(result, "Can not find %s of enum %s", value, enumEntityClass);
        return result;
    }
}
