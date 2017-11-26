package com.pi.common.persistence.mybatis.plugins;

import com.pi.common.persistence.mybatis.aop.annotations.OptimisticLocking;
import com.pi.common.utils.exceptions.PiRuntimeException;
import com.pi.common.utils.i18n.CrudCode;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Intercepts(@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }))
public class OptimisticLockingInterceptor implements Interceptor {

    private static final int MAPPED_STATEMENT_INDEX = 0;

    private static final ConcurrentMap<String, OptimisticLocking> REQUIRE_OPTIMISTIC_LOCKING_STATEMENT_CACHE = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation)
            throws Throwable {

        Object[] args = invocation.getArgs();

        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        if (mappedStatement.getSqlCommandType() != SqlCommandType.DELETE
            && mappedStatement.getSqlCommandType() != SqlCommandType.UPDATE) {
            return invocation.proceed();
        }

        OptimisticLocking optimisticLocking = REQUIRE_OPTIMISTIC_LOCKING_STATEMENT_CACHE.computeIfAbsent(mappedStatement.getId(),
                this::findOptimisticLocking);
        if (optimisticLocking == null) {
            return invocation.proceed();
        }

        Object result = invocation.proceed();
        if (result.getClass() != Integer.class) {
            throw new IllegalStateException("Result type of OptimisticLocking annotated method must be Integer. Actual return type is "
                                                   + result.getClass());
        }
        if (((Integer) result) == 0) {
            throw new PiRuntimeException(optimisticLocking.value(), optimisticLocking.requireMessage() ? CrudCode.DATA_MODIFIED : null);
        }
        return result;
    }

    private OptimisticLocking findOptimisticLocking(String statementId) {
        try {
            int index = statementId.lastIndexOf(".");
            String className = statementId.substring(0, index);
            String methodName = statementId.substring(index + 1);

            Class<?> clazz = Class.forName(className);

            Class<?>[] paramTypes = null;
            Method targetMethod = ClassUtils.getMethodIfAvailable(clazz, methodName, paramTypes);
            if (targetMethod == null) {
                throw new IllegalArgumentException("Can not find target method of statement id " + statementId + " in " + clazz);
            }
            return targetMethod.getAnnotation(OptimisticLocking.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
