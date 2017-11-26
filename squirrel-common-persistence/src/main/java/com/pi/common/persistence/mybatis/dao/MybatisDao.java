package com.pi.common.persistence.mybatis.dao;

import java.io.Serializable;

public interface MybatisDao<T, ID extends Serializable>
        extends QueryDao<T, ID>, CreationDao<T>, DeletionDao<T, ID>, UpdateDao<T> {
}
