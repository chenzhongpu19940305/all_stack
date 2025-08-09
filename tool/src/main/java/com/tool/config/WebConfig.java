package com.tool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 
 * @author tool-service
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源与本地uploads映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:./");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 全局CORS
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition", "content-disposition")
                .allowCredentials(false)
                .maxAge(3600);
    }
}