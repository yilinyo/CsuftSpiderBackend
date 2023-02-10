package com.yilin.csuftspider.config;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
/**
 * @ClassName JedisConfig
 * @Description TODO
 * @Author admin
 * @Version 1.0
 */
@Configuration
public class JedisConfig {
 
    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);
 
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
    public JedisPool  jedisPool(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
 
        JedisPool jedisPool=new JedisPool(jedisPoolConfig,host,port,timeout,password);
 
        logger.info("JedisPool连接成功:"+host+":\t 端口号："+port);
 
        return jedisPool;
    }
}