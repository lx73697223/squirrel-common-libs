package com.pi.common.persistence.mybatis.dao;

import com.pi.common.utils.aop.annotation.ExecuteByBatch;

import java.io.Serializable;
import java.util.List;

public interface QueryDao<T, ID extends Serializable> {

    T findOne(ID id);

    List<T> findAll();

    @ExecuteByBatch
    List<T> findAllByIds(Iterable<ID> ids);

    boolean exists(ID id);

    long countAll();

}
