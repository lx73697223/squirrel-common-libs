package com.pi.common.persistence.mybatis.dao;

import com.pi.common.persistence.mybatis.aop.annotations.OptimisticLocking;

import java.io.Serializable;
import java.util.Collection;

public interface OptimisticLockingDeletionDao<T, ID extends Serializable> {

    @OptimisticLocking
    int deleteById(ID id);

    @OptimisticLocking
    int delete(T entity);

    @OptimisticLocking
    int deleteBatch(Collection<? extends T> entities);

    @OptimisticLocking
    int deleteAll();
}
