package com.pi.common.autoconfigure.sequence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pi.common.utils.core.sequence.SnowflakeIdGenerator;
import com.pi.common.utils.core.sequence.UniqueIdGenerator;

@Configuration
@ConditionalOnProperty(prefix = "spring.application", name = "instance-id")
public class DefaultUniqueIdGeneratorConfiguration {

    @Value("${spring.application.instance-id}")
    private int instanceId;

    @Bean
    @ConditionalOnProperty(prefix = "pi.sequence", name = "id-generator", havingValue = "snowflake", matchIfMissing = true)
    public UniqueIdGenerator snowflakeIdGenerator() {
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(instanceId);
        return idGenerator;
    }

}
