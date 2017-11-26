package com.pi.common.utils.enums;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ClassPathEnumEntityScanner extends ClassPathScanningCandidateComponentProvider {

    public ClassPathEnumEntityScanner() {
        super(false);
        addIncludeFilter(new AssignableTypeFilter(EnumEntity.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }

}
