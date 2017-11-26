package com.pi.common.persistence.mybatis.dialect;

public class SQLServerDialect implements Dialect {

    @Override
    public String getLimitString(String sql, int offset, int limit) {

        StringBuilder limitBuilder = new StringBuilder(sql.length() + 50);
        limitBuilder.append(sql);
        limitBuilder.append(" offset ");
        if (offset > 0) {
            limitBuilder.append(offset);
        } else {
            limitBuilder.append(0);
        }
        limitBuilder.append(" rows fetch next ");
        limitBuilder.append(limit);
        limitBuilder.append(" rows only");

        return limitBuilder.toString();
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }
}
