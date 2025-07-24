package com.example.langchain.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
// import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

/**
 * LangChain配置类
 * 配置聊天模型和嵌入模型
 * 
 * @author AI Assistant
 * @version 1.0.0
 */
@Configuration
public class LangChainConfig {

    @Value("${langchain4j.open-ai.chat-model.api-key:}")
    private String openAiApiKey;

    @Value("${langchain4j.open-ai.chat-model.model-name:gpt-3.5-turbo}")
    private String chatModelName;

    @Value("${langchain4j.open-ai.chat-model.temperature:0.7}")
    private Double temperature;

    @Value("${langchain4j.open-ai.chat-model.max-tokens:1000}")
    private Integer maxTokens;

    /**
     * 配置OpenAI聊天模型
     * 如果没有配置API Key，将返回一个默认实现
     */
    @Bean
    @Primary
    public ChatLanguageModel chatLanguageModel() {
        if (openAiApiKey == null || openAiApiKey.isEmpty() || "your-openai-api-key-here".equals(openAiApiKey)) {
            System.out.println("警告: 未配置OpenAI API Key，使用默认聊天模型");
            System.out.println("请在application.yml中配置OPENAI_API_KEY环境变量以启用完整功能");
            // 返回一个默认实现，避免Spring注入失败
            return new ChatLanguageModel() {
                @Override
                public dev.langchain4j.model.output.Response<dev.langchain4j.data.message.AiMessage> generate(java.util.List<dev.langchain4j.data.message.ChatMessage> messages) {
                    dev.langchain4j.data.message.AiMessage aiMessage = dev.langchain4j.data.message.AiMessage.from("抱歉，聊天功能暂时不可用。请配置OpenAI API Key。");
                    return dev.langchain4j.model.output.Response.from(aiMessage);
                }
            };
        }
        
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(chatModelName)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    /**
     * 配置嵌入模型
     * 使用OpenAI嵌入模型（如果配置了API Key）
     */
    @Bean
    @Primary
    public EmbeddingModel embeddingModel() {
        if (openAiApiKey != null && !openAiApiKey.isEmpty() && !"your-openai-api-key-here".equals(openAiApiKey)) {
            return OpenAiEmbeddingModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName("text-embedding-ada-002")
                    .timeout(Duration.ofSeconds(60))
                    .build();
        }
        
        System.out.println("警告: 未配置OpenAI API Key，使用默认嵌入模型");
        // 返回一个默认实现，避免Spring注入失败
        return new EmbeddingModel() {
            @Override
            public dev.langchain4j.model.output.Response<dev.langchain4j.data.embedding.Embedding> embed(String text) {
                // 返回一个空的嵌入向量响应
                dev.langchain4j.data.embedding.Embedding embedding = dev.langchain4j.data.embedding.Embedding.from(new float[]{0.0f});
                return dev.langchain4j.model.output.Response.from(embedding);
            }
            
            @Override
            public dev.langchain4j.model.output.Response<java.util.List<dev.langchain4j.data.embedding.Embedding>> embedAll(java.util.List<dev.langchain4j.data.segment.TextSegment> textSegments) {
                // 返回空的嵌入向量列表
                java.util.List<dev.langchain4j.data.embedding.Embedding> embeddings = new java.util.ArrayList<>();
                for (int i = 0; i < textSegments.size(); i++) {
                    embeddings.add(dev.langchain4j.data.embedding.Embedding.from(new float[]{0.0f}));
                }
                return dev.langchain4j.model.output.Response.from(embeddings);
            }
        };
    }
}