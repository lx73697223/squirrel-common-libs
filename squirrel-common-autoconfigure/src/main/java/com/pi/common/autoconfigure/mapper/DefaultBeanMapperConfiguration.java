package com.pi.common.autoconfigure.mapper;

import com.pi.common.utils.mapper.bean.BeanMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultBeanMapperConfiguration {

    @Bean
    public MapperFactory mapperFactory() {
        return BeanMapper.createDefaultMapperFactory();
    }

    @Bean
    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }

}
