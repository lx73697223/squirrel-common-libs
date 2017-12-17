package com.pi.common.utils.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class BaseEntity implements Serializable, LogicalDeletionEntity {

    private String createdBy;

    private ZonedDateTime createdTime;

    private String updatedBy;

    private ZonedDateTime updatedTime;

    private boolean deleted = false;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    public void setCreatedBy(Object id) {
        this.createdBy = (id == null) ? null : id.toString();
    }

    public void setUpdatedBy(Object id) {
        this.updatedBy = (id == null) ? null : id.toString();
    }
}
