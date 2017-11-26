package com.pi.common.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestProperties foo() {
        TestProperties properties = new TestProperties();
        properties.setName("from test config");
        return properties;
    }

}
