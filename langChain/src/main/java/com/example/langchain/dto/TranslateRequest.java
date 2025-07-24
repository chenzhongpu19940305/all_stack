package com.example.langchain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 翻译请求DTO
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
public class TranslateRequest {

    @NotBlank(message = "要翻译的文本不能为空")
    @Size(max = 1000, message = "文本长度不能超过1000字符")
    private String text;

    @NotBlank(message = "目标语言不能为空")
    private String targetLanguage;

    public TranslateRequest() {}

    public TranslateRequest(String text, String targetLanguage) {
        this.text = text;
        this.targetLanguage = targetLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    @Override
    public String toString() {
        return "TranslateRequest{" +
                "text='" + text + '\'' +
                ", targetLanguage='" + targetLanguage + '\'' +
                '}';
    }
}