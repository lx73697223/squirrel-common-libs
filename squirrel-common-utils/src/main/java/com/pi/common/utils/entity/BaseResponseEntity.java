package com.pi.common.utils.entity;

public class BaseResponseEntity<T> {

    private T result;

    public BaseResponseEntity() {
    }

    public BaseResponseEntity(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> BaseResponseEntity<T> ok(T body) {
        return new BaseResponseEntity<>(body);
    }

}
