package com.pi.common.utils.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.ClassUtils;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.pi.common.utils.i18n.MessageSourceAccessorHolder.getAccessor;

public interface MessageCode {

    ConcurrentMap<Class<?>, ConcurrentMap<Integer, String>> MESSAGE_CODE_NAME_CACHE = new ConcurrentHashMap<>();

    int getId();

    default String getMessageCode() {
        return MESSAGE_CODE_NAME_CACHE.computeIfAbsent(getClass(), c -> new ConcurrentHashMap<>()).computeIfAbsent(getId(),
                i -> StringUtils.substringBefore(ClassUtils.getShortName(getClass()), ".") + "_" + i);
    }

    default String getMessage() {
        Object[] args = null;
        return getMessage(args);
    }

    default String getMessage(Locale locale) {
        Object[] args = null;
        return getMessage(locale, args);
    }

    default String getMessage(Object... args) {
        return getMessage(null, args);
    }

    default String getMessage(Locale locale, Object... args) {
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }
        return getAccessor().getMessage(getMessageCode(), args, locale);
    }

    default MessageSourceResolvable createMessageSourceResolvable(Object... args) {
        return new DefaultMessageSourceResolvable(new String[] { getMessageCode() }, args);
    }
}
