package com.example.langchain;

import com.example.langchain.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LangChain应用程序测试类
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
class LangChainApplicationTests {

    @Autowired
    private ChatService chatService;

    @Test
    void contextLoads() {
        // 测试Spring上下文是否正常加载
        assertNotNull(chatService);
    }

    @Test
    void testChatServiceInitialization() {
        // 测试ChatService是否正确初始化
        assertNotNull(chatService);
        
        // 注意：在测试环境中，如果没有配置API Key，chatService.isAvailable()可能返回false
        // 这是正常的，因为我们没有在测试中配置真实的API Key
    }

    @Test
    void testChatServiceWithoutApiKey() {
        // 测试在没有API Key的情况下，服务是否能正常处理请求
        String response = chatService.chat("Hello");
        assertNotNull(response);
        
        // 如果没有配置API Key，应该返回提示信息
        if (!chatService.isAvailable()) {
            assertTrue(response.contains("请配置OpenAI API Key") || response.contains("不可用"));
        }
    }
}