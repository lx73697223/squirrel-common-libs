package com.pi.common.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface EnumEntity<T> {

    @JsonProperty("id")
    T getValue();

    default T getAltLookupValue() {
        return getValue();
    }
}
