package com.pi.common.persistence.mybatis.dialect;

public interface Dialect {

    String getLimitString(String sql, int offset, int limit);

    boolean supportsLimit();

    boolean supportsLimitOffset();
}
