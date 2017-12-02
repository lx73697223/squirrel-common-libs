package com.pi.common.utils.enums;

public interface EnumEntity<T> {

    T getValue();

    default T getAltLookupValue() {
        return getValue();
    }
}
