package com.pi.common.persistence.mybatis.typehandle;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityFactory;
import com.pi.common.utils.core.ReflectionUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

public class EnumEntityTypeHandler<T extends EnumEntity<?>> extends BaseTypeHandler<T> {

    private final Map<Object, EnumEntity<?>> enumMap;

    private final TypeHandler<Object> typeHandler;

    @SuppressWarnings("unchecked")
    private EnumEntityTypeHandler(TypeHandlerRegistry typeHandlerRegistry, Class<T> enumEntityClass) {
        this.enumMap = EnumEntityFactory.getEnumMap(enumEntityClass);
        ParameterizedType interfaceType = ReflectionUtils.findSpecificGenericInterfaces(enumEntityClass);
        Class<?> valueType = (Class<?>) interfaceType.getActualTypeArguments()[0];
        this.typeHandler = (TypeHandler<Object>) typeHandlerRegistry.getTypeHandler(valueType);
    }

    public static <T extends EnumEntity<?>> EnumEntityTypeHandler<T> create(TypeHandlerRegistry typeHandlerRegistry,
            Class<T> enumClass) {
        return new EnumEntityTypeHandler<>(typeHandlerRegistry, enumClass);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        typeHandler.setParameter(ps, i, parameter.getValue(), jdbcType);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertResult(typeHandler.getResult(rs, columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertResult(typeHandler.getResult(rs, columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertResult(typeHandler.getResult(cs, columnIndex));
    }

    @SuppressWarnings("unchecked")
    private T convertResult(Object result) {
        return result == null ? null : (T) enumMap.get(result);
    }

}
