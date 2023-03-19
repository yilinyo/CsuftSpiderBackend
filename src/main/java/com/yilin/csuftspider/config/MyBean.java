package com.yilin.csuftspider.config;

import com.yilin.csuftspider.utils.Session;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.misc.MessageUtils;

/**
 * Title: MyBean
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-02-26
 */
@Configuration

public class MyBean {

    @Bean
    public Session session(){

        return new Session();

    }
}
