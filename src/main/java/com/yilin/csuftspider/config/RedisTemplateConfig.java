package com.yilin.csuftspider.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.yilin.csuftspider.mapper.JacksonObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Title: RedisTemplateConfig
 * Description: TODO
 * jackson 的序列化
 * @author Yilin
 * @version V1.0
 * @date 2023-01-15
 */

@Configuration

public class RedisTemplateConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public GenericObjectPoolConfig getPoolConfig(){
        // 配置redis连接池
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);

        return poolConfig;
    }





    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){




//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//
//
//        return  redisTemplate;
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);



        // 使用StringRedisSerializer来序列化和反序列化Redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化Redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 配置对象映射器
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();
        // 指定要序列化的域，field，get和set，以及修饰符范围。ANY指包括private和public修饰符范围
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入类型，类的信息也将添加到json中，这样才可以根据类名反序列化。
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        // 将对象映射器添加到序列化器中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 配置key，value，hashKey，hashValue的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);



        return redisTemplate;




    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate1(){





//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//
//
//        return  redisTemplate;
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();




        // 构建工厂对象
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(RedisPassword.of(password));
        LettucePoolingClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(timeout)).poolConfig(getPoolConfig()).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, clientConfiguration);
        // 设置使用的redis数据库
        factory.setDatabase(1);


        // 重新初始化工厂
        factory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(factory);


        // 设置使用的redis数据库






        // 使用StringRedisSerializer来序列化和反序列化Redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化Redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 配置对象映射器
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();
        // 指定要序列化的域，field，get和set，以及修饰符范围。ANY指包括private和public修饰符范围
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入类型，类的信息也将添加到json中，这样才可以根据类名反序列化。
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        // 将对象映射器添加到序列化器中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 配置key，value，hashKey，hashValue的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);



        return redisTemplate;




    }


}
