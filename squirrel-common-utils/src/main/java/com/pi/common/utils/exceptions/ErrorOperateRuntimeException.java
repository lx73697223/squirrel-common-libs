package com.pi.common.utils.exceptions;

import com.pi.common.utils.i18n.ValidationCode;
import org.springframework.http.HttpStatus;

public class ErrorOperateRuntimeException extends PiRuntimeException {

    public ErrorOperateRuntimeException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ErrorOperateRuntimeException(HttpStatus httpStatus) {
        super(httpStatus, ValidationCode.ERROR_OPERATE);
    }

}
