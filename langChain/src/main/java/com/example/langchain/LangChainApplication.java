package com.example.langchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * LangChain Spring Boot应用程序主类
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@SpringBootApplication
public class LangChainApplication {

    public static void main(String[] args) {
        System.out.println("aaaaaaaaaaaaaaaaaa");
        SpringApplication.run(LangChainApplication.class, args);
        System.out.println("\n=== LangChain Spring Boot应用启动成功 ===");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("========================================\n");
    }
}