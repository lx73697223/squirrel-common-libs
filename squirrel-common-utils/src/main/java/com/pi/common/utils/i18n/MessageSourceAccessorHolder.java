package com.pi.common.utils.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

public class MessageSourceAccessorHolder implements MessageSourceAware {

    private static MessageSourceAccessor accessor;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    public static MessageSourceAccessor getAccessor() {
        return accessor;
    }
}
