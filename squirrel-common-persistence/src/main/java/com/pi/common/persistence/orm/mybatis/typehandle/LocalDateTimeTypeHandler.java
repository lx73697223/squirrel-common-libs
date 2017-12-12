package com.pi.common.persistence.orm.mybatis.typehandle;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalDateTime;

public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.DATE) {
            ps.setDate(i, Date.valueOf(parameter.toLocalDate()));
        } else {
            ps.setTimestamp(i, Timestamp.valueOf(parameter));
        }
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getLocalDateTime(rs.getTimestamp(columnName));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getLocalDateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getLocalDateTime(cs.getTimestamp(columnIndex));
    }

    private LocalDateTime getLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
