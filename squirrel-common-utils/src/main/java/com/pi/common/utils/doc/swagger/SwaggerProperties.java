package com.pi.common.utils.doc.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = SwaggerProperties.PREFIX)
public class SwaggerProperties {

    public static final String PREFIX = "swagger";

    private Boolean enable = true;

    private Boolean needToken = true;

    private String title = "";

    private String version = "";

    private String termsOfServiceUrl = "";

    private String contactName = "";

    private String contactUrl = "";

    private String contactEmail = "";

    private String license = "";

    private String licenseUrl = "";

    private String groupName = "";

    private String description = "";

    private String scanBasePackage = "";

    private String regexPath;

}
