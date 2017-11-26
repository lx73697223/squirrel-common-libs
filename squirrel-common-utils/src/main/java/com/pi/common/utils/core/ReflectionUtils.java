package com.pi.common.utils.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ReflectionUtils {

    public static Class<?> resolveTypeArgument(Class<?> clazz) {
        for (Class<?> userClass = ClassUtils.getUserClass(clazz), superClass = userClass
                .getSuperclass(); superClass != null; userClass = superClass, superClass = userClass.getSuperclass()) {
            Class<?> result = GenericTypeResolver.resolveTypeArgument(userClass, superClass);
            if (result != null) {
                return result;
            }
        }
        throw new IllegalArgumentException("Can not find ParameterizedType of class " + clazz.getName());
    }

    public static ParameterizedType findSpecificGenericInterfaces(Class<?> clazz) {
        ParameterizedType type = doFindSpecificGenericInterfaces(clazz);
        if (type == null) {
            throw new IllegalArgumentException("Can not find ParameterizedType of class " + clazz.getName());
        }
        return type;
    }

    private static ParameterizedType doFindSpecificGenericInterfaces(Class<?> clazz) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                return (ParameterizedType) type;
            } else if (type instanceof Class) {
                ParameterizedType result = doFindSpecificGenericInterfaces((Class<?>) type);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static Class<? extends Object> getClass(ProceedingJoinPoint jp)
            throws NoSuchMethodException {
        return jp.getTarget().getClass();
    }

    public static Method getMethod(ProceedingJoinPoint jp)
            throws NoSuchMethodException {
        org.aspectj.lang.Signature sig = jp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        return getClass(jp).getMethod(msig.getName(), msig.getParameterTypes());
    }

}
