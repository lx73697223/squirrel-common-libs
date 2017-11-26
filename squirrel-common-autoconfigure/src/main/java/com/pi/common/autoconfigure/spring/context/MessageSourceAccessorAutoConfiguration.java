package com.pi.common.autoconfigure.spring.context;

import com.pi.common.utils.i18n.MessageSourceAccessorHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
@ConditionalOnClass({ MessageSourceAccessor.class, MessageSourceAccessorHolder.class })
public class MessageSourceAccessorAutoConfiguration {

    @Bean
    public MessageSourceAccessorHolder messageSourceAccessorHolder() {
        return new MessageSourceAccessorHolder();
    }
}
