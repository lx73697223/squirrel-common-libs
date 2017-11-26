package com.pi.common.utils.exceptions;

import com.pi.common.utils.i18n.MessageCode;
import org.springframework.http.HttpStatus;

import java.util.Locale;

public class PiRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -3688028536617382355L;

    private final HttpStatus status;

    private final MessageCode messageCode;

    private final Object[] args;

    public PiRuntimeException(HttpStatus status) {
        this(status, null);
    }

    public PiRuntimeException(HttpStatus status, MessageCode messageCode, Object... args) {
        this(status, null, messageCode, args);
    }

    public PiRuntimeException(HttpStatus status, Throwable cause, MessageCode messageCode, Object... args) {
        super(cause);
        this.status = status;
        this.messageCode = messageCode;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        return getMessage(null);
    }

    public String getMessage(Locale locale) {
        if (getMessageCode() == null) {
            return super.getMessage();
        } else {
            return getMessageCode().getMessage(locale, getArgs());
        }
    }

    public MessageCode getMessageCode() {
        return messageCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
