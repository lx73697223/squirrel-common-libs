package com.pi.common.persistence.mybatis.dao;

public interface UpdateDao<T> {

    int update(T entity);
}
