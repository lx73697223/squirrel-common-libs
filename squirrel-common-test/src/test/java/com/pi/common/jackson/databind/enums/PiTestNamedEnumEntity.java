package com.pi.common.jackson.databind.enums;

import com.pi.common.utils.enums.NamedEnumEntity;

public enum PiTestNamedEnumEntity implements NamedEnumEntity<Byte> {

    ENUM_ONE(1, "壹", "一"),

    ENUM_TWO(2, "贰", "二"),

    ENUM_THREE(3, "叁", "三");

    private byte value;

    private String name;

    private String shortName;

    private PiTestNamedEnumEntity(int value, String name, String shortName) {
        this.value = (byte) value;
        this.name = name;
        this.shortName = shortName;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortName() {
        return shortName;
    }
}
