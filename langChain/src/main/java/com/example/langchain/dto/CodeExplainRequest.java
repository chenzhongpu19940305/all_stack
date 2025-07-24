package com.example.langchain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 代码解释请求DTO
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
public class CodeExplainRequest {

    @NotBlank(message = "代码内容不能为空")
    @Size(max = 2000, message = "代码长度不能超过2000字符")
    private String code;

    @NotBlank(message = "编程语言不能为空")
    private String language;

    public CodeExplainRequest() {}

    public CodeExplainRequest(String code, String language) {
        this.code = code;
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "CodeExplainRequest{" +
                "code='" + code + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}