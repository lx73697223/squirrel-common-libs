package com.pi.common.utils.exceptions;

import com.pi.common.utils.i18n.SecurityCode;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends PiRuntimeException {

    public UnAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED, SecurityCode.REQUIRE_PERMISSION);
    }
}
