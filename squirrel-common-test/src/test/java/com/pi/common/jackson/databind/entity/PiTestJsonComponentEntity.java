package com.pi.common.jackson.databind.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class PiTestJsonComponentEntity {

    private Long id;

    @JsonSerialize(using = PiTestUrlJsonSerializer.class)
    private String photoUrl;

}
