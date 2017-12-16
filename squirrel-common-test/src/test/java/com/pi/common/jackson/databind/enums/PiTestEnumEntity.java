package com.pi.common.jackson.databind.enums;

import com.pi.common.utils.enums.EnumEntity;

public enum PiTestEnumEntity implements EnumEntity<Byte> {

    ENUM_ONE(1),
    ENUM_TWO(2),
    ENUM_THREE(3);

    private byte value;

    private PiTestEnumEntity(int value) {
        this.value = (byte) value;
    }

    @Override
    public Byte getValue() {
        return value;
    }

}
