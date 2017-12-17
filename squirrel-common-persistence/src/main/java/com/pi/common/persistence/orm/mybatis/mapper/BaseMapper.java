package com.pi.common.persistence.orm.mybatis.mapper;

import com.pi.common.utils.entity.BaseEntity;
import com.pi.common.utils.entity.FieldsExpandParam;
import com.pi.common.utils.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface BaseMapper<T extends BaseEntity, ID extends Serializable> {

    int saveBatch(@Param("list") List<T> list);

    int deleteBatch(@Param("list") Collection<T> list);

    int deleteByIds(@Param("ids") Collection<ID> ids);

    int update(@Param("entity") T entity);

    List<T> findByIds(@Param("ids") Collection<ID> ids,
                      @Param("fieldsExpandParam") FieldsExpandParam fieldsExpandParam);

    <P extends Page> long countPage(@Param("page") P page);

    <P extends Page> List<T> findPage(@Param("page") P page,
                                      @Param("rowBounds") RowBounds rowBounds);

}
