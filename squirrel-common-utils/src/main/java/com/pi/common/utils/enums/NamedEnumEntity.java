package com.pi.common.utils.enums;

public interface NamedEnumEntity<T> extends EnumEntity<T> {

    String getName();

    default String getShortName() {
        return null;
    }

}
