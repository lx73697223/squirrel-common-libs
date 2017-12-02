package com.pi.common.jackson.autoconfigure;

import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pi.common.jackson.databind.EnumEntityJsonComponent;
import com.pi.common.jackson.mapper.JacksonJsonMapper;
import com.pi.common.utils.enums.EnumEntityUtils;
import org.springframework.beans.factory.annotation.Value;
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

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jackson")
public class PiJacksonAutoConfiguration {

    @Value("${jackson.enum-entity-packages}")
    private String[] enumEntityPackages;

    public void setEnumEntityPackages(String[] enumEntityPackages) {
        this.enumEntityPackages = enumEntityPackages;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer piJackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // serializer
            builder.serializationInclusion(JsonInclude.Include.NON_EMPTY).failOnUnknownProperties(
                    false).featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // deserializer
            EnumEntityUtils.scanEnumEntityPackages(type -> {
                EnumEntityJsonComponent.Deserializer deserializer = new EnumEntityJsonComponent.Deserializer(type);
                builder.deserializerByType(type, deserializer);
            }, enumEntityPackages);
        };
    }

    @Bean
    public JacksonJsonMapper jsonMapper(ObjectMapper objectMapper) {
        return new JacksonJsonMapper(objectMapper);
    }

    @Configuration
    @ConditionalOnClass({ XmlMapper.class, WstxOutputFactory.class })
    @ConditionalOnBean(Jackson2ObjectMapperBuilder.class)
    protected static class JacksonXmlMapperConfiguration {

        @Bean
        public XmlMapper jacksonXmlMapper(Jackson2ObjectMapperBuilder builder) {

            XmlMapper xmlMapper = builder.createXmlMapper(true).build();
            XmlFactory xmlFactory = xmlMapper.getFactory();
            WstxOutputFactory xmlOutputFactory = (WstxOutputFactory) xmlFactory
                                                                             .getXMLOutputFactory();
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
