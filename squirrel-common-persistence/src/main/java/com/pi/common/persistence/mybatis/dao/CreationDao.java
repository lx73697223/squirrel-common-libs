package com.pi.common.persistence.mybatis.dao;

import java.util.Collection;

public interface CreationDao<T> {

    int save(T entity);

    int saveBatch(Collection<T> entities);
}
