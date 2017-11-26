package com.pi.common.utils.aop.annotation;

import java.lang.annotation.*;

/**
 * 需要分批执行的参数必须为集合且在第一个位置
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExecuteByBatch {

    int value() default 1000;

}
