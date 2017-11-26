package com.pi.common.utils.exceptions;

import com.pi.common.utils.i18n.MessageCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadInputException extends PiRuntimeException {

    public BadInputException() {
        this(null);
    }

    public BadInputException(MessageCode messageCode, Object... args) {
        super(HttpStatus.BAD_REQUEST, messageCode, args);
    }
}
