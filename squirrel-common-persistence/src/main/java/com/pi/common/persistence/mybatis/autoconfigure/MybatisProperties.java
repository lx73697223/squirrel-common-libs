package com.pi.common.persistence.mybatis.autoconfigure;

import lombok.Data;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {

    public static final String PREFIX = "mybatis";

    private boolean cacheEnabled = false;

    private boolean useGeneratedKeys = true;

    private ExecutorType defaultExecutorType = ExecutorType.REUSE;

    private boolean mapUnderscoreToCamelCase = true;

    private String logImpl = "SLF4J";

    /**
     Config file path.
     */
    private String config;

    /**
     Location of mybatis mapper files.
     */
    private Resource[] mapperLocations;

    /**
     Package to scan domain objects.
     */
    private String[] typeAliasesPackages;

    /**
     Package to scan handlers.
     */
    private String[] typeHandlersPackages;

    /**
     Execution mode.
     */
    private ExecutorType executorType = defaultExecutorType;

    /**
     Package to scan EnumEntities.
     */
    private String[] enumEntityPackages;

    private boolean paginationEnabled = true;

}
