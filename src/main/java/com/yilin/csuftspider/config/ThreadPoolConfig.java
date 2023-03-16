package com.yilin.csuftspider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync // 允许使用异步方法
public class ThreadPoolConfig {

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        threadPoolTaskExecutor.setCorePoolSize(2);
        // 设置最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(3);
        // 设置工作队列大小
        threadPoolTaskExecutor.setQueueCapacity(2000);
        // 设置线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("threadPoolTaskExecutor-->");
        // 设置拒绝策略.当工作队列已满,线程数为最大线程数的时候,接收新任务抛出RejectedExecutionException异常
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 初始化线程池
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
