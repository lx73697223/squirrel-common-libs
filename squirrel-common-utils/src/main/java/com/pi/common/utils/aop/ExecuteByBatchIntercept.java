package com.pi.common.utils.aop;

import com.pi.common.utils.aop.annotation.ExecuteByBatch;
import com.pi.common.utils.core.IteratorUtils;
import com.pi.common.utils.core.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Aspect
@Component
public class ExecuteByBatchIntercept {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteByBatchIntercept.class);

    @Pointcut("@annotation(com.pi.common.utils.aop.annotation.ExecuteByBatch)")
    public void pointCut() {
    }

    @SuppressWarnings("unchecked")
    @Around(value = "pointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();
        if (args == null || args.length == 0 || args[0] == null) {
            return pjp.proceed();
        }

        Collection<Object> args0 = (Collection<Object>) args[0];
        if (IteratorUtils.isEmpty(args0)) {
            return pjp.proceed();
        }

        Method method = ReflectionUtils.getMethod(pjp);
        ExecuteByBatch anno = method.getAnnotation(ExecuteByBatch.class);
        int batchSize = anno.value();

        List<Object> listArg = new ArrayList<>(args0);
        int listSize = IteratorUtils.size(listArg);
        if (listSize <= batchSize) {
            return pjp.proceed();
        }

        // 只有返回值为集合或整型时需要累加结果, 其他类型不需要
        Boolean isCollectionType = null;
        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            isCollectionType = true;
        } else {
            if (returnType.isPrimitive()) {
                if (returnType.getName().equals("long") || returnType.getName().equals("int")) {
                    isCollectionType = false;
                }
            } else if (Number.class.isAssignableFrom(returnType)) {
                isCollectionType = false;
            }
        }

        Boolean isCollectionTypeFinal = isCollectionType;
        Collection<Object> list = new ArrayList<>();

        Consumer<List<Object>> executer = ids -> {
            try {
                args[0] = ids;
                Object result = pjp.proceed(args);

                if (isCollectionTypeFinal != null) {
                    if (isCollectionTypeFinal) {
                        list.addAll((List<Object>) result);
                    } else {
                        list.add(result);
                    }
                }
            } catch (Throwable e) {
                throw new RuntimeException("ExecuteByBatch proceed occur exception.", e);
            }
        };

        IteratorUtils.executeByBatch(listArg, batchSize, executer);

        LOGGER.info("ExecuteByBatch method:{}, listSize:{}, batchSize:{}.", method.getName(), listSize, batchSize);

        if (isCollectionTypeFinal == null){
            return null;
        }
        return isCollectionTypeFinal ? list : list.stream().mapToLong(n -> Long.parseLong(n.toString())).sum();
    }

}
