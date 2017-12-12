package com.pi.common.persistence.orm.mybatis.typehandle;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(i, Time.valueOf(parameter));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getLocalTime(rs.getTime(columnName));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getLocalTime(rs.getTime(columnIndex));
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getLocalTime(cs.getTime(columnIndex));
    }

    private LocalTime getLocalTime(Time time) {
        return time == null ? null : time.toLocalTime();
    }
}
