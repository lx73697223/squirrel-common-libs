package com.pi.common.autoconfigure.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisAutoConfiguration {

    @Bean
    public GenericToStringSerializer<Object> genericToStringSerializer() {
        return new GenericToStringSerializer<>(Object.class);
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJsonRedisSerializer() {
        return genericJsonRedisSerializer();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJsonRedisSerializer(ObjectMapper objectMapper) {
        ObjectMapper redisJsonMapper = objectMapper.copy();
        redisJsonMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(redisJsonMapper);
    }

    @Bean
    public RedisTemplate<String, ?> jsonRedisTemplate(RedisConnectionFactory connectionFactory,
                                                             GenericJackson2JsonRedisSerializer genericJsonRedisSerializer) {

        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(genericToStringSerializer());
        redisTemplate.setValueSerializer(genericJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(genericToStringSerializer());
        redisTemplate.setHashValueSerializer(genericJsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplateForCaching(RedisConnectionFactory factory, ObjectMapper objectMapper) {

        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper om = objectMapper.copy();

        /**
         * 反序列化时,遇到未知属性(那些没有对应的属性来映射的属性,并且没有任何setter或handler来处理这样的属性)时是否引起结果失败(通过抛JsonMappingException异常).
         * 此项设置只对那些已经尝试过所有的处理方法之后并且属性还是未处理(这里未处理的意思是:最终还是没有一个对应的类属性与此属性进行映射)的未知属性才有影响.
         * 此功能默认是启用的(意味着,如果遇到未知属性时会抛一个JsonMappingException)
         */
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        /* 控制序列化 和 反序列化 作用范围 */
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);

        /* 为了不让没有指定class对象的json可以返回需要的对象 而不是不返回一个Map对象  */
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @ConditionalOnClass(Session.class)
    @ConditionalOnWebApplication
    @AutoConfigureAfter(RedisAutoConfiguration.class)
    protected static class RedisActionAutoConfiguration {

        @Bean
        public static ConfigureRedisAction configureRedisAction() {
            return ConfigureRedisAction.NO_OP;
        }
    }
}
