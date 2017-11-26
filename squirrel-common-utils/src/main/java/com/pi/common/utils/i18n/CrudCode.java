package com.pi.common.utils.i18n;

public enum CrudCode implements MessageCode {

    DATA_ADD_SUCCESS(101),
    DATA_UPDATE_SUCCESS(102),
    DATA_DELETE_SUCCESS(103),
    NO_DATA_FOUND(201),
    DATA_EXIST(202),
    DATA_MODIFIED(203),
    DATA_REMOVED(204),
    DATA_ADD_FAILED(205),
    DATA_UPDATE_FAILED(206),
    DATA_FIND_TOO_MANY_RESULT(207),
    INVALID_RELATION(301),
    NONLOCALIZED_MESSAGE(401);

    private int id;

    private CrudCode(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
