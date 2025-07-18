# Tool Service

这是一个基于 Spring Boot 2.7.18 的工具服务项目，使用 JDK 1.8。

## 项目结构

```
tool/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tool/
│   │   │           ├── ToolServiceApplication.java    # 主启动类
│   │   │           ├── controller/                   # 控制器层
│   │   │           │   ├── HelloController.java
│   │   │           │   └── UserController.java
│   │   │           ├── entity/                       # 实体类
│   │   │           │   └── User.java
│   │   │           ├── repository/                   # 数据访问层
│   │   │           │   └── UserRepository.java
│   │   │           └── service/                      # 服务层
│   │   │               ├── UserService.java
│   │   │               └── impl/
│   │   │                   └── UserServiceImpl.java
│   │   └── resources/
│   │       └── application.yml                       # 配置文件
│   └── test/
│       └── java/
│           └── com/
│               └── tool/
│                   └── ToolServiceApplicationTests.java
├── pom.xml                                           # Maven 配置文件
└── README.md                                         # 项目说明
```

## 技术栈

- **Spring Boot**: 2.7.18
- **Java**: 1.8
- **Maven**: 构建工具
- **Spring Data JPA**: 数据访问层
- **MySQL**: 数据库
- **Lombok**: 简化代码
- **Spring Boot DevTools**: 开发工具

## 功能特性

- 用户管理（CRUD 操作）
- RESTful API 设计
- 数据库集成
- 健康检查接口
- 完整的错误处理

## API 接口

### 健康检查
- `GET /api/hello/health` - 健康检查
- `GET /api/hello/welcome` - 欢迎信息

### 用户管理
- `POST /api/users` - 创建用户
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 根据ID获取用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户
- `GET /api/users/search?keyword=xxx` - 搜索用户

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE tool_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tool_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 运行项目

1. 克隆项目到本地
2. 进入项目目录：`cd tool`
3. 编译项目：`mvn clean compile`
4. 运行项目：`mvn spring-boot:run`
5. 访问：`http://localhost:8080/api/hello/health`

### 测试

运行测试：
```bash
mvn test
```

## 开发说明

### 项目特点

- 使用 ES5 语法，避免 ES6 可选链操作符
- 完整的 JavaDoc 注释
- 统一的异常处理
- 标准的 RESTful API 设计
- 分层架构设计

### 代码规范

- 类名使用大驼峰命名法
- 方法名使用小驼峰命名法
- 常量使用全大写加下划线
- 包名使用小写字母
- 所有公共方法都需要添加 JavaDoc 注释

## 部署

### 打包

```bash
mvn clean package
```

### 运行 JAR 文件

```bash
java -jar target/tool-service-0.0.1-SNAPSHOT.jar
```

## 许可证

MIT License 