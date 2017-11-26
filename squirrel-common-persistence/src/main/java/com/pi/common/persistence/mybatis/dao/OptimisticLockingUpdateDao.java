package com.pi.common.persistence.mybatis.dao;

import com.pi.common.persistence.mybatis.aop.annotations.OptimisticLocking;

public interface OptimisticLockingUpdateDao<T> {

    @OptimisticLocking
    int update(T entity);
}
