package com.yilin.csuftspider;

import com.yilin.csuftspider.utils.BarkUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication

public class CsuftSpiderBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsuftSpiderBackendApplication.class, args);
    }

}
