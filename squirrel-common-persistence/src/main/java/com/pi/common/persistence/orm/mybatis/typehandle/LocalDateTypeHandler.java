package com.pi.common.persistence.orm.mybatis.typehandle;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.TIMESTAMP) {
            ps.setTimestamp(i, Timestamp.valueOf(parameter.atStartOfDay()));
        } else {
            ps.setDate(i, Date.valueOf(parameter));
        }
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getLocalDate(rs.getDate(columnName));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getLocalDate(rs.getDate(columnIndex));
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getLocalDate(cs.getDate(columnIndex));
    }

    private LocalDate getLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }
}
