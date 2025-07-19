package com.tool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot 主启动类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.tool"})
public class ToolServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolServiceApplication.class, args);
    }
} 