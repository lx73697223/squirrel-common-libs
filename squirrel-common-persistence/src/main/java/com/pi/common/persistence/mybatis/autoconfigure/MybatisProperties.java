package com.pi.common.persistence.mybatis.autoconfigure;

import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

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
    private ExecutorType executorType;

    /**
     Package to scan EnumEntities.
     */
    private String[] enumEntityPackages;

    private boolean paginationEnabled = true;

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public boolean isUseGeneratedKeys() {
        return useGeneratedKeys;
    }

    public void setUseGeneratedKeys(boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
    }

    public ExecutorType getDefaultExecutorType() {
        return defaultExecutorType;
    }

    public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
        this.defaultExecutorType = defaultExecutorType;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public String getLogImpl() {
        return logImpl;
    }

    public void setLogImpl(String logImpl) {
        this.logImpl = logImpl;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Resource[] getMapperLocations() {
        return this.mapperLocations;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String[] getTypeHandlersPackages() {
        return this.typeHandlersPackages;
    }

    public void setTypeHandlersPackages(String[] typeHandlersPackages) {
        this.typeHandlersPackages = typeHandlersPackages;
    }

    public String[] getTypeAliasesPackages() {
        return this.typeAliasesPackages;
    }

    public void setTypeAliasesPackages(String[] typeAliasesPackages) {
        this.typeAliasesPackages = typeAliasesPackages;
    }

    public ExecutorType getExecutorType() {
        return this.executorType == null ? getDefaultExecutorType() : this.executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public String[] getEnumEntityPackages() {
        return enumEntityPackages;
    }

    public void setEnumEntityPackages(String[] enumEntityPackages) {
        this.enumEntityPackages = enumEntityPackages;
    }

    public boolean isPaginationEnabled() {
        return paginationEnabled;
    }

    public void setPaginationEnabled(boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

}
