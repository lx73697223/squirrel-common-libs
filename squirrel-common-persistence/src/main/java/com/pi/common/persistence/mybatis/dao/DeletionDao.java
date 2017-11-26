package com.pi.common.persistence.mybatis.dao;

import java.io.Serializable;
import java.util.Collection;

public interface DeletionDao<T, ID extends Serializable> {

    int deleteById(ID id);

    int delete(T entity);

    int deleteBatch(Collection<? extends T> entities);

    int deleteAll();
}
