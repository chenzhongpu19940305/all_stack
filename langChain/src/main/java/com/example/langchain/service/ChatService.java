package com.example.langchain.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

/**
 * 聊天服务类
 * 提供基础的AI聊天功能
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@Service
public class ChatService {

    private final ChatLanguageModel chatLanguageModel;

    @Autowired
    public ChatService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    /**
     * 简单聊天
     * 
     * @param message 用户消息
     * @return AI回复
     */
    public String chat(String message) {
        if (chatLanguageModel == null) {
            return "抱歉，聊天功能暂时不可用。请配置OpenAI API Key。";
        }
        
        try {
            UserMessage userMessage = UserMessage.from(message);
            List<ChatMessage> messages = Arrays.asList(userMessage);
            return chatLanguageModel.generate(messages).content().text();
        } catch (Exception e) {
            return "聊天服务出现错误: " + e.getMessage();
        }
    }

    /**
     * 使用模板进行聊天
     * 
     * @param templateText 模板文本
     * @param variables 模板变量
     * @return AI回复
     */
    public String chatWithTemplate(String templateText, Map<String, Object> variables) {
        if (chatLanguageModel == null) {
            return "抱歉，聊天功能暂时不可用。请配置OpenAI API Key。";
        }
        
        try {
            PromptTemplate promptTemplate = PromptTemplate.from(templateText);
            Prompt prompt = promptTemplate.apply(variables);
            UserMessage userMessage = UserMessage.from(prompt.text());
            List<ChatMessage> messages = Arrays.asList(userMessage);
            return chatLanguageModel.generate(messages).content().text();
        } catch (Exception e) {
            return "模板聊天服务出现错误: " + e.getMessage();
        }
    }

    /**
     * 翻译服务
     * 
     * @param text 要翻译的文本
     * @param targetLanguage 目标语言
     * @return 翻译结果
     */
    public String translate(String text, String targetLanguage) {
        String template = "请将以下文本翻译成{{language}}:\n\n{{text}}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("language", targetLanguage);
        variables.put("text", text);
        
        return chatWithTemplate(template, variables);
    }

    /**
     * 代码解释服务
     * 
     * @param code 代码片段
     * @param language 编程语言
     * @return 代码解释
     */
    public String explainCode(String code, String language) {
        String template = "请解释以下{{language}}代码的功能和逻辑:\n\n```{{language}}\n{{code}}\n```";
        Map<String, Object> variables = new HashMap<>();
        variables.put("language", language);
        variables.put("code", code);
        
        return chatWithTemplate(template, variables);
    }

    /**
     * 检查聊天服务是否可用
     * 
     * @return 是否可用
     */
    public boolean isAvailable() {
        return chatLanguageModel != null;
    }
}