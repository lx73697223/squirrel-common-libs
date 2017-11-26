/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.pi.common.utils.spring;

import org.springframework.core.env.AbstractEnvironment;

/**
 Spring profile 常用方法与profile名称。 */
public class Profiles {

    public static final String DEFAULT = "default";

    public static final String PRODUCTION = "production";

    public static final String EXCEPT_PRODUCTION = "!production";

    public static final String STABLE = "stable";

    public static final String INTEGRATION = "integration";

    public static final String DEVELOPMENT = "development";

    public static final String UNIT_TEST = "test";

    public static final String EXCEPT_UNIT_TEST = "!test";

    public static final String FUNCTIONAL_TEST = "functional";

    /**
     在Spring启动前，设置profile的环境变量。
     */
    public static void setProfileAsSystemProperty(String profile) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
    }

}
