package com.pi.common.utils.entity;

import lombok.Data;

@Data
public class Page extends FieldsExpandParam {

    private int pageNo = 1;

    private int pageSize = -1;

    private String orderBy = null;

    private String order = null;

}
