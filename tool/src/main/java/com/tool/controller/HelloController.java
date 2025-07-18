package com.tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello Controller 示例
 * 
 * @author tool-service
 * @version 1.0.0
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * 健康检查接口
     * 
     * @return 健康状态信息
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", "UP");
        result.put("message", "Tool Service is running");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 欢迎接口
     * 
     * @return 欢迎信息
     */
    @GetMapping("/welcome")
    public Map<String, Object> welcome() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", "Welcome to Tool Service!");
        result.put("version", "1.0.0");
        return result;
    }
} 