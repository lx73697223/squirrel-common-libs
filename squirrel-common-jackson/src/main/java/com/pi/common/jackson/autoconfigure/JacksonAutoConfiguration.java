package com.pi.common.jackson.autoconfigure;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.pi.common.jackson.databind.EnumEntityDeserializer;
import com.pi.common.jackson.databind.EnumEntitySerializer;
import com.pi.common.jackson.databind.NamedEnumEntitySerializer;
import com.pi.common.jackson.mapper.JacksonJsonMapper;
import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityUtils;
import com.pi.common.utils.enums.NamedEnumEntity;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jackson")
public class JacksonAutoConfiguration {

    private String[] enumEntityPackages;

    public void setEnumEntityPackages(String[] enumEntityPackages) {
        this.enumEntityPackages = enumEntityPackages;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer squirrelJackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializationInclusion(Include.NON_EMPTY).failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .serializerByType(EnumEntity.class, new EnumEntitySerializer())
                    .serializerByType(NamedEnumEntity.class, new NamedEnumEntitySerializer());

            EnumEntityUtils.scanEnumEntityPackages(type -> {
                EnumEntityDeserializer deserializer = new EnumEntityDeserializer(type);
                builder.deserializerByType(type, deserializer);
            }, enumEntityPackages);
        };
    }

    @Bean
    public JacksonJsonMapper jsonMapper(ObjectMapper objectMapper) {
        return new JacksonJsonMapper(objectMapper);
    }

    @Bean
    public JacksonJsonMapper snakeCaseJsonMapper(ObjectMapper objectMapper) {
        ObjectMapper snakeCaseObjectMapper = snakeCaseObjectMapper(objectMapper);
        return new JacksonJsonMapper(snakeCaseObjectMapper);
    }

    @Bean
    public ObjectMapper snakeCaseObjectMapper(ObjectMapper objectMapper) {
        ObjectMapper snakeCaseObjectMapper = objectMapper.copy();
        snakeCaseObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return snakeCaseObjectMapper;
    }

    @Configuration
    @ConditionalOnClass({ XmlMapper.class, WstxOutputFactory.class })
    @ConditionalOnBean(Jackson2ObjectMapperBuilder.class)
    protected static class JacksonXmlMapperConfiguration {

        @Bean
        public XmlMapper jacksonXmlMapper(Jackson2ObjectMapperBuilder builder) {

            XmlMapper xmlMapper = builder.createXmlMapper(true).build();
            XmlFactory xmlFactory = xmlMapper.getFactory();
            WstxOutputFactory xmlOutputFactory = (WstxOutputFactory) xmlFactory.getXMLOutputFactory();
            xmlOutputFactory.configureForSpeed();

            return xmlMapper;
        }
    }

    @Configuration
    @ConditionalOnClass({ XmlMapper.class, MappingJackson2XmlHttpMessageConverter.class })
    @AutoConfigureAfter(JacksonXmlMapperConfiguration.class)
    protected static class MappingJackson2XmlHttpMessageConverterConfiguration {

        @Bean
        public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(XmlMapper xmlMapper) {
            return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
        }
    }

}
