package com.example.langchain.controller;

import com.example.langchain.dto.ChatRequest;
import com.example.langchain.dto.ChatResponse;
import com.example.langchain.dto.TranslateRequest;
import com.example.langchain.dto.CodeExplainRequest;
import com.example.langchain.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天控制器
 * 提供LangChain相关的REST API接口
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("chatServiceAvailable", chatService.isAvailable());
        response.put("message", "LangChain服务运行正常");
        return ResponseEntity.ok(response);
    }

    /**
     * 简单聊天接口
     */
    @PostMapping("/simple")
    public ResponseEntity<ChatResponse> simpleChat(@Valid @RequestBody ChatRequest request) {
        try {
            String response = chatService.chat(request.getMessage());
            return ResponseEntity.ok(new ChatResponse(true, response, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse(false, null, "聊天服务异常: " + e.getMessage()));
        }
    }

    /**
     * 翻译接口
     */
    @PostMapping("/translate")
    public ResponseEntity<ChatResponse> translate(@Valid @RequestBody TranslateRequest request) {
        try {
            String response = chatService.translate(request.getText(), request.getTargetLanguage());
            return ResponseEntity.ok(new ChatResponse(true, response, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse(false, null, "翻译服务异常: " + e.getMessage()));
        }
    }

    /**
     * 代码解释接口
     */
    @PostMapping("/explain-code")
    public ResponseEntity<ChatResponse> explainCode(@Valid @RequestBody CodeExplainRequest request) {
        try {
            String response = chatService.explainCode(request.getCode(), request.getLanguage());
            return ResponseEntity.ok(new ChatResponse(true, response, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse(false, null, "代码解释服务异常: " + e.getMessage()));
        }
    }

    /**
     * 获取服务状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("chatServiceAvailable", chatService.isAvailable());
        status.put("timestamp", LocalDateTime.now());
        
        if (chatService.isAvailable()) {
            status.put("message", "所有服务正常运行");
        } else {
            status.put("message", "聊天服务不可用，请检查OpenAI API Key配置");
        }
        
        return ResponseEntity.ok(status);
    }
}