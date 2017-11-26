package com.pi.common.persistence.mybatis.dao;

import java.io.Serializable;

public interface OptimisticLockingMybatisDao<T, ID extends Serializable> extends QueryDao<T, ID>, CreationDao<T>,
        OptimisticLockingDeletionDao<T, ID>, OptimisticLockingUpdateDao<T> {
}
