package com.pi.common.utils.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseEntity<T> {

    private T result;

    public static <T> BaseResponseEntity<T> ok(T body) {
        return new BaseResponseEntity<>(body);
    }

}
