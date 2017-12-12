package com.pi.common.persistence.orm.mybatis.typehandle;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.DATE) {
            ps.setDate(i, Date.valueOf(parameter.toLocalDate()));
        } else {
            ps.setTimestamp(i, Timestamp.valueOf(parameter.toLocalDateTime()));
        }
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return getZonedDateTime(timestamp);
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return getZonedDateTime(timestamp);
    }

    @Override
    public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return getZonedDateTime(timestamp);
    }

    private static ZonedDateTime getZonedDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().atZone(ZoneOffset.UTC);
    }
}
