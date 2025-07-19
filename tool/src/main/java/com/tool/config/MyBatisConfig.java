package com.tool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Configuration
public class MyBatisConfig {
    
    /**
     * 注册SQL日志拦截器
     */
    @Bean
    public SimpleSqlLogInterceptor sqlLogInterceptor() {
        return new SimpleSqlLogInterceptor();
    }
} 