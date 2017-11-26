package com.pi.common.utils.sql;

import com.google.common.base.CaseFormat;
import com.pi.common.utils.core.IteratorUtils;
import com.pi.common.utils.exceptions.PiRuntimeException;
import com.pi.common.utils.i18n.CrudCode;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public final class SqlUtils {

    private static ConcurrentMap<Class<?>, String> ENTITY_TABLE_NAME_MAP = new ConcurrentHashMap<>();

    private static ConcurrentMap<String, Set<String>> TABLE_COLUMN_NAMES_MAP = new ConcurrentHashMap<>();

    private SqlUtils() {
    }

    public static void appendSqlLikeSymbol(String name, Consumer<String> setterMethod) {
        setterMethod.accept(StringUtils.appendIfMissing(name, "%"));
    }

    public static String appendSqlLikeSymbol(String sql) {
        return appendSqlLikeSymbol(sql, true, true);
    }

    public static String appendSqlLikeSymbol(String sql, boolean appendPrefix) {
        return appendSqlLikeSymbol(sql, appendPrefix, true);
    }

    public static String appendSqlLikeSymbol(String sql, boolean appendPrefix, boolean appendSuffix) {

        if (StringUtils.isBlank(sql)) {
            return null;
        }

        StringBuilder likeBuilder = new StringBuilder(sql.length() + 2);
        if (appendPrefix) {
            likeBuilder.append("%");
        }
        likeBuilder.append(sql);
        if (appendSuffix) {
            likeBuilder.append("%");
        }
        return likeBuilder.toString();
    }

    public static void validateColumnNameExistence(String tableName, String columnName, DataSource dataSource) {
        Set<String> tableColumnsName = getTableColumnsName(tableName, dataSource);
        if (!tableColumnsName.contains(columnName)) {
            throw new PiRuntimeException(HttpStatus.BAD_REQUEST, CrudCode.INVALID_RELATION, columnName, tableName);
        }
    }

    public static String getEntityTableName(Class<?> entityClass) {
        return ENTITY_TABLE_NAME_MAP.computeIfAbsent(entityClass,
                key -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key.getSimpleName()));
    }

    public static Set<String> getTableColumnsName(Class<?> entityClass, DataSource dataSource) {
        String tableName = getEntityTableName(entityClass);
        return getTableColumnsName(tableName, dataSource);
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getTableColumnsName(String tableName, DataSource dataSource) {
        return TABLE_COLUMN_NAMES_MAP.computeIfAbsent(tableName, key -> {
            try {
                return (Set<String>) JdbcUtils.extractDatabaseMetaData(dataSource, dbmd -> {
                    Set<String> columns = Collections.newSetFromMap(new CaseInsensitiveMap<String, Boolean>());
                    ResultSet columnsResultSet = dbmd.getColumns(null, null, key, null);
                    while (columnsResultSet.next()) {
                        columns.add(columnsResultSet.getString("COLUMN_NAME"));
                    }
                    if (IteratorUtils.isNotEmpty(columns)) {
                        return columns;
                    }

                    columnsResultSet = dbmd.getColumns(null, null, key.toUpperCase(Locale.ENGLISH), null);
                    while (columnsResultSet.next()) {
                        columns.add(columnsResultSet.getString("COLUMN_NAME"));
                    }
                    if (IteratorUtils.isEmpty(columns)) {
                        throw new IllegalArgumentException("Can not find columns of table " + key);
                    }
                    return columns;
                });
            } catch (MetaDataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String appendSqlSearchSymbol(String value) {
        return appendSqlSearchSymbol(value, true, true);
    }

    public static String appendSqlSearchSymbol(String value, boolean appendPrefix) {
        return appendSqlSearchSymbol(value, appendPrefix, true);
    }

    public static String appendSqlSearchSymbol(String value, boolean appendPrefix, boolean appendSuffix) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        // '"*value*"', ‘"’双引号是必须要有的, ‘*’星号表示把分词当作前缀或后缀
        StringBuilder likeBuilder = new StringBuilder(value.length() + 4);
        likeBuilder.append("\"");
        if (appendPrefix) {
            likeBuilder.append("*");
        }
        likeBuilder.append(value);
        if (appendSuffix) {
            likeBuilder.append("*");
        }
        likeBuilder.append("\"");
        return likeBuilder.toString();
    }

}
