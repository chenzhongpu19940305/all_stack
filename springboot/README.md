# Spring Boot 2.7.18 Web 项目

这是一个基于 Spring Boot 2.7.18 的完整 Web 应用，集成了 MySQL、MyBatis、MVC 等功能。

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── demo/
│   │               ├── DemoApplication.java
│   │               ├── controller/
│   │               │   ├── HelloController.java (REST API)
│   │               │   └── WebController.java (Web 页面)
│   │               ├── entity/
│   │               │   └── User.java
│   │               ├── mapper/
│   │               │   └── UserMapper.java
│   │               └── service/
│   │                   ├── UserService.java
│   │                   └── impl/
│   │                       └── UserServiceImpl.java
│   └── resources/
│       ├── application.yml
│       └── templates/
│           ├── index.html
│           ├── users.html
│           └── user-form.html
└── test/
    └── java/
        └── com/
            └── example/
                └── demo/
                    └── DemoApplicationTests.java
```

## 功能特性

- **REST API**: 提供用户 CRUD 操作的 RESTful API
- **Web 页面**: 基于 Thymeleaf 的用户管理界面
- **数据库**: MySQL 数据库支持
- **ORM**: MyBatis 和 JPA 双重支持
- **验证**: 表单验证和数据校验
- **开发工具**: 热重载和开发工具支持

## 数据库配置

在运行项目前，请确保：

1. 安装并启动 MySQL 数据库
2. 创建数据库：`CREATE DATABASE demo;`
3. 修改 `application.yml` 中的数据库连接信息

## 运行项目

### 使用 Maven
```bash
mvn spring-boot:run
```

### 打包运行
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 访问地址

启动应用后，可以访问以下地址：

### Web 页面
- **首页**: http://localhost:8080/
- **用户管理**: http://localhost:8080/users
- **添加用户**: http://localhost:8080/users/new

### REST API
- **API 测试**: http://localhost:8080/api/hello
- **获取所有用户**: GET http://localhost:8080/api/users
- **获取单个用户**: GET http://localhost:8080/api/users/{id}
- **创建用户**: POST http://localhost:8080/api/users
- **更新用户**: PUT http://localhost:8080/api/users/{id}
- **删除用户**: DELETE http://localhost:8080/api/users/{id}

#### 算法学习 API
- **获取所有算法**: GET http://localhost:8080/api/algorithms
- **获取单个算法**: GET http://localhost:8080/api/algorithms/{id}
- **创建算法**: POST http://localhost:8080/api/algorithms
- **更新算法**: PUT http://localhost:8080/api/algorithms/{id}
- **删除算法**: DELETE http://localhost:8080/api/algorithms/{id}
- **按分类获取**: GET http://localhost:8080/api/algorithms/category/{category}
- **获取所有分类**: GET http://localhost:8080/api/algorithms/categories
- **搜索算法**: GET http://localhost:8080/api/algorithms/search?name={name}

## 技术栈

- **Spring Boot 2.7.18**
- **Spring MVC**
- **Spring Data JPA**
- **MyBatis**
- **MySQL**
- **Thymeleaf**
- **Bootstrap 5**
- **Lombok**
- **Java 8**
- **Maven**

## API 示例

### 创建用户
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

### 获取所有用户
```bash
curl http://localhost:8080/api/users
``` 