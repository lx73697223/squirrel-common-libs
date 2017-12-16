package com.pi.common.persistence.orm.mybatis.plugins;

import com.pi.common.persistence.orm.mybatis.dialect.Dialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@Intercepts(@Signature(type = Executor.class, method = "query",
        args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }))
public class OffsetLimitInterceptor implements Interceptor {

    private static final int MAPPED_STATEMENT_INDEX = 0;

    private static final int PARAMETER_INDEX = 1;

    private static final int ROW_BOUNDS_INDEX = 2;

    private Dialect dialect;

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public Object intercept(Invocation invocation)
            throws InvocationTargetException, IllegalAccessException {
        processIntercept(invocation);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

        String dialectClass = properties.getProperty("dialectClass");
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create dialect instance by dialectClass: " + dialectClass, e);
        }
    }

    private void processIntercept(Invocation invocation) {

        Object[] args = invocation.getArgs();

        MappedStatement mappedStatement = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object parameter = args[PARAMETER_INDEX];
        RowBounds rowBounds = (RowBounds) args[ROW_BOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        if (limit <= 0) {
            return;
        }

        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();
            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }

            args[ROW_BOUNDS_INDEX] = RowBounds.DEFAULT;

            BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sql);

            MappedStatement newMappedStatement = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
            args[MAPPED_STATEMENT_INDEX] = newMappedStatement;
        }
    }

    private BoundSql copyFromBoundSql(MappedStatement mappedStatement, BoundSql boundSql, String sql) {

        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql, boundSql.getParameterMappings(),
                                                   boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement mappedStatement, SqlSource newSqlSource) {

        Builder builder = new Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), newSqlSource,
                                             mappedStatement.getSqlCommandType());

        builder.resource(mappedStatement.getResource());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.statementType(mappedStatement.getStatementType());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        StringBuilder sb = new StringBuilder();
        String[] keyProperties = mappedStatement.getKeyProperties();
        if (keyProperties != null) {
            for (String keyProperty : mappedStatement.getKeyProperties()) {
                sb.append(keyProperty);
                sb.append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            builder.keyProperty(sb.toString());
        }

        // setStatementTimeout()
        builder.timeout(mappedStatement.getTimeout());

        // setParameterMap()
        builder.parameterMap(mappedStatement.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.resultSetType(mappedStatement.getResultSetType());

        // setStatementCache()
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());

        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
