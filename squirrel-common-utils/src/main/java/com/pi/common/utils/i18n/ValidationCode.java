package com.pi.common.utils.i18n;

public enum ValidationCode implements MessageCode {

    VALUE_REQUIRED(101),
    VALUE_TOO_LONG(102),
    FUTURE_DATE(103),
    INVALID_FORMAT(104),
    FATAL_ERROR(201),
    ALREADY_EXISTS(202),
    NOT_EXIST(203),
    NOT_EXIST_IN_ASSOCIATED_DATA(204),
    INVALID_SEARCH_DATE(205),
    FILE_NOT_EXIST(206),
    EMPTY_FILE(207),
    ERROR_OPERATE(208),
    NOT_ENOUGH_POINT(303);

    private int id;

    private ValidationCode(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
