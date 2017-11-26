package com.pi.common.utils.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface LogMethodReturn {

    String loggerName() default "";

    Level level() default Level.DEBUG;

    LogContent[] logContent() default LogContent.COST_TIME;

    enum Level {
        DEBUG,
        INFO,
        WARN
    }

    enum LogContent {
        COST_TIME,
        RETURN_CONTENT,
        ARGS
    }

}
