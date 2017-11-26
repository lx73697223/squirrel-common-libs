package com.pi.common.utils.core;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public final class FutureUtils {

    public static <T> T tryGet(Future<? extends T> future, Supplier<? extends T> defaultValueGetter, Logger optionalLogger) {
        try {
            return future.get();
        } catch (Throwable th) {
            if (optionalLogger != null) {
                optionalLogger.error("Error when getting result from a Future.  exception.message:{}, stackTrace:{}",
                        ExceptionUtils.getMessage(th), ExceptionUtils.getStackTrace(th));
            }
            return defaultValueGetter.get();
        }

    }
}
