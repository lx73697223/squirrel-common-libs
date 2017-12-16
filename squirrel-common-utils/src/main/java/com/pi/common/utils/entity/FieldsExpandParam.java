package com.pi.common.utils.entity;

import lombok.Data;

import java.util.Set;

@Data
public class FieldsExpandParam {

    // 指定需要获取的字段
    private String fields;

    // 关联查询
    private Set<String> expands;

}
