# LangChain Spring Boot 项目

这是一个基于Spring Boot和LangChain4j的基础AI应用项目，提供了聊天、翻译、代码解释等功能。

## 技术栈

- **Java**: 17
- **Spring Boot**: 3.2.0
- **LangChain4j**: 0.25.0
- **Maven**: 项目构建工具

## 功能特性

- ✅ AI聊天对话
- ✅ 文本翻译
- ✅ 代码解释
- ✅ 本地嵌入模型支持
- ✅ OpenAI模型集成
- ✅ RESTful API接口
- ✅ 健康检查

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+

### 2. 配置API Key（可选）

如果要使用OpenAI模型，需要配置API Key：

```bash
# 设置环境变量
export OPENAI_API_KEY=your-actual-api-key

# 或者在application.yml中直接配置
langchain4j:
  open-ai:
    chat-model:
      api-key: your-actual-api-key
```

**注意**: 如果不配置OpenAI API Key，项目仍然可以运行，但聊天功能将不可用。嵌入功能会使用本地模型。

### 3. 运行项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/langchain-springboot-1.0.0.jar
```

### 4. 访问应用

- 应用地址: http://localhost:8080
- 健康检查: http://localhost:8080/api/chat/health
- 服务状态: http://localhost:8080/api/chat/status

## API接口

### 健康检查
```http
GET /api/chat/health
```

### 简单聊天
```http
POST /api/chat/simple
Content-Type: application/json

{
  "message": "你好，请介绍一下自己"
}
```

### 文本翻译
```http
POST /api/chat/translate
Content-Type: application/json

{
  "text": "Hello, how are you?",
  "targetLanguage": "中文"
}
```

### 代码解释
```http
POST /api/chat/explain-code
Content-Type: application/json

{
  "code": "public static void main(String[] args) { System.out.println(\"Hello World\"); }",
  "language": "Java"
}
```

### 服务状态
```http
GET /api/chat/status
```

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/langchain/
│   │       ├── LangChainApplication.java      # 主启动类
│   │       ├── config/
│   │       │   └── LangChainConfig.java       # LangChain配置
│   │       ├── controller/
│   │       │   └── ChatController.java        # REST控制器
│   │       ├── service/
│   │       │   └── ChatService.java           # 聊天服务
│   │       └── dto/
│   │           ├── ChatRequest.java           # 聊天请求DTO
│   │           ├── ChatResponse.java          # 聊天响应DTO
│   │           ├── TranslateRequest.java      # 翻译请求DTO
│   │           └── CodeExplainRequest.java    # 代码解释请求DTO
│   └── resources/
│       └── application.yml                    # 应用配置
└── pom.xml                                    # Maven配置
```

## 配置说明

### application.yml 主要配置项

```yaml
# 服务端口
server:
  port: 8080

# LangChain4j配置
langchain4j:
  open-ai:
    chat-model:
      api-key: ${OPENAI_API_KEY:your-openai-api-key-here}
      model-name: gpt-3.5-turbo
      temperature: 0.7
      max-tokens: 1000
```

## 开发指南

### 添加新的AI功能

1. 在 `ChatService` 中添加新的方法
2. 在 `ChatController` 中添加对应的API端点
3. 创建相应的DTO类（如果需要）
4. 更新API文档

### 集成其他AI模型

项目支持多种AI模型，可以在 `LangChainConfig` 中配置：

- OpenAI GPT系列
- Azure OpenAI
- Hugging Face模型
- 本地模型

## 故障排除

### 常见问题

1. **聊天功能不可用**
   - 检查OpenAI API Key是否正确配置
   - 确认网络连接正常
   - 查看应用日志获取详细错误信息

2. **嵌入模型加载失败**
   - 确保有足够的内存
   - 检查依赖是否正确下载

3. **端口冲突**
   - 修改 `application.yml` 中的端口配置

### 查看日志

```bash
# 查看应用日志
tail -f logs/spring.log

# 或者在控制台查看实时日志
mvn spring-boot:run
```

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目！