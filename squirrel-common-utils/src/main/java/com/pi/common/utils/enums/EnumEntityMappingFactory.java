package com.pi.common.utils.enums;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EnumEntityMappingFactory {

    private static final ConcurrentMap<EnumEntity<?>, EnumEntityMapping<?, EnumEntity<?>>> ENUM_ENTITY_MAPPING_CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends EnumEntityMapping<?, ? extends EnumEntity<?>>> T getUnchecked(Class<T> enumEntityMappingClass, EnumEntity<?> enumEntity) {
        if (enumEntity == null) {
            return null;
        }
        return (T) ENUM_ENTITY_MAPPING_CACHE.computeIfAbsent(enumEntity, key -> {
            EnumEntityMapping<?, ? extends EnumEntity<?>>[] enumConstants = enumEntityMappingClass.getEnumConstants();

            for (EnumEntityMapping<?, ? extends EnumEntity<?>> enumEntityMapping : enumConstants) {
                if (Objects.equals(key, enumEntityMapping.getCorrespondingEnumEntity())) {
                    return (EnumEntityMapping<?, EnumEntity<?>>) enumEntityMapping;
                }
            }
            return null;
        });
    }

    public static <T extends EnumEntityMapping<?, EnumEntity<?>>> T get(Class<T> enumEntityMappingClass, EnumEntity<?> enumEntity) {
        T result = getUnchecked(enumEntityMappingClass, enumEntity);
        Objects.requireNonNull(result, () -> String.format("Can not find correspoinding EnumEntityMapping in %s of EnumEntity %s",
                enumEntityMappingClass, enumEntity));
        return result;
    }
}
