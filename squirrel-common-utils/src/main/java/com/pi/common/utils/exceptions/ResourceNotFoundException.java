package com.pi.common.utils.exceptions;

import com.pi.common.utils.i18n.MessageCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends PiRuntimeException {

    public ResourceNotFoundException() {
        this(null);
    }

    public ResourceNotFoundException(MessageCode messageCode, Object... args) {
        super(HttpStatus.NOT_FOUND, messageCode, args);
    }
}
