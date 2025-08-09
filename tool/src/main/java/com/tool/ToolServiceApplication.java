package com.tool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 主启动类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.tool.mapper")
public class ToolServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolServiceApplication.class, args);
    }
}