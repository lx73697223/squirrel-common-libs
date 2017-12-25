package com.pi.common.utils.doc.swagger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = SwaggerProperties.PREFIX, name = "enable")
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties properties;

    @Bean
    public Docket docket() {
        return createDocket();
    }

    protected Docket createDocket() {
        return createDocket(pathsRegex(properties.getRegexPath()), properties.getGroupName(), properties.getScanBasePackage(),
                createApiInfo(), properties.getNeedToken(), properties.getEnable());
    }

    protected ApiInfo createApiInfo() {
        return new ApiInfo(properties.getTitle(), properties.getDescription(), properties.getVersion(),
                properties.getTermsOfServiceUrl(),
                new Contact(properties.getContactName(), properties.getContactUrl(), properties.getContactEmail()),
                properties.getLicense(), properties.getLicenseUrl());
    }

    protected Docket createDocket(Predicate<String> pathsRegex, String groupName, String basePackage, ApiInfo apiInfo,
            boolean isNeedToken, boolean enable) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).enable(enable);
        docket.genericModelSubstitutes(ResponseEntity.class).useDefaultResponseMessages(true);
        docket.groupName(groupName).apiInfo(apiInfo);
        docket.select().apis(RequestHandlerSelectors.basePackage(basePackage)).paths(pathsRegex).build();
        if (isNeedToken) {
            docket.globalOperationParameters(getGlobalOperationParameters());
        }
        return docket;
    }

    protected List<Parameter> getGlobalOperationParameters() {
        List<Parameter> list = new ArrayList<>();
        Parameter auth = new ParameterBuilder().name("Authorization").description("Authorization")
                .modelRef(new ModelRef("string")).parameterType("header").defaultValue("Bearer ").required(true).build();
        list.add(auth);
        return list;
    }

    protected Predicate<String> pathsRegex(String regexPath) {
        return StringUtils.isBlank(regexPath) ? PathSelectors.any() : PathSelectors.regex(regexPath);
    }

}
