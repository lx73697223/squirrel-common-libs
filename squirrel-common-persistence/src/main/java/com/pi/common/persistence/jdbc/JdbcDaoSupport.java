package com.pi.common.persistence.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ClassUtils;

public abstract class JdbcDaoSupport<T> {

    protected JdbcTemplate jdbcTemplate;

    protected RowMapper<T> rowMapper;

    public JdbcDaoSupport() {
        Class<?> daoClass = ClassUtils.getUserClass(getClass());
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(daoClass, daoClass.getSuperclass());
        this.rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
