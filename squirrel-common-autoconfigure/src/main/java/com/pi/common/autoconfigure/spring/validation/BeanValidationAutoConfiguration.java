package com.pi.common.autoconfigure.spring.validation;

import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

@ConditionalOnClass(Validator.class)
public class BeanValidationAutoConfiguration implements MessageSourceAware {

    @Setter
    private MessageSource messageSource;

    @Bean
    @ConditionalOnClass(MethodValidationPostProcessor.class)
    public MethodValidationPostProcessor methodValidationPostProcessor(Validator validator) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator);
        methodValidationPostProcessor.setProxyTargetClass(true);

        return methodValidationPostProcessor;
    }

    @Bean
    @ConditionalOnClass(LocalValidatorFactoryBean.class)
    public Validator validator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        validatorFactoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");

        return validatorFactoryBean;
    }
}
