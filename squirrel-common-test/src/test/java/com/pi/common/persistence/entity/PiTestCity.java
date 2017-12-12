package com.pi.common.persistence.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PiTestCity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String state;

    private String country;

}
