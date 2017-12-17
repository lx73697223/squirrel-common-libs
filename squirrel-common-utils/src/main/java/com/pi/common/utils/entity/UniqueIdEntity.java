package com.pi.common.utils.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniqueIdEntity extends BaseEntity {

    private String uniqueId;

    public void setUniqueId(Object id){
        this.uniqueId = (id == null) ? null : id.toString();
    }

}
