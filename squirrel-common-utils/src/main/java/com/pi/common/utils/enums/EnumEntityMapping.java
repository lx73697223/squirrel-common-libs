package com.pi.common.utils.enums;

public interface EnumEntityMapping<T, E extends EnumEntity<?>> extends EnumEntity<T> {

    E getCorrespondingEnumEntity();

}
