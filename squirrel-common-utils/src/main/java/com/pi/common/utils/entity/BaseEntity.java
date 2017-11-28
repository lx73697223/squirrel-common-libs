package com.pi.common.utils.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class BaseEntity implements Serializable, LogicalDeletionEntity {

    private Long createdBy;

    private ZonedDateTime createdTime;

    private Long updatedBy;

    private ZonedDateTime updatedTime;

    @JsonIgnore
    private boolean deleted = false;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

}
