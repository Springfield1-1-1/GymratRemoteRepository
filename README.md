# GymRat 健身达人

## 项目简介

本项目旨在为爱健身运动的群体打造一个干净整洁、亲于用户方便使用、免费无盈利的专属网站，对用户提供以下核心功能：

- 健身入门指南
- 器械分类及教学
- 热量需求计算
- 用户自定义健身计划
- 查找周边门店及教练
- 个人中心

网站基于springboot、vue、mysql、redis等技术搭建，保证了易扩展、易维护、易部署

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0.33
- **ORM 框架**: MyBatis Plus 3.5.9
- **缓存**: Redis
- **安全认证**: JWT
- **文件存储**: 阿里云 OSS
- **API 文档**: SpringDoc OpenAPI
- **监控**: Spring Boot Admin
- **其他**: Lombok, WebSocket, Validation

## 主要功能模块

### 1. 用户认证模块

- 用户注册与登录
- JWT Token 认证
- 用户信息管理（头像上传、个人信息修改）

### 2. 管理员模块

- 数据概览统计
- 用户、器械、门店管理
- 客服工作平台

### 3. 教练管理模块

- 教练信息查询
- 教练信息管理
- 教练头像上传

### 4. 健身房门店模块

- 门店列表查询
- 门店详情查询
- 门店信息管理
- 门店图片上传

### 5. 器械管理模块

- 器械分类管理
- 器械信息查询（支持分页、分类筛选、关键词搜索）
- 器械信息管理
- 器械图片上传

### 6. 健身计划模块

- 健身计划创建与保存
- 健身计划查询与更新
- 健身计划删除

## 项目结构

```
src/main/java/com/springfield/gymrat/
├── common/                 # 通用组件
│   ├── context/           # 用户上下文
│   ├── exception/         # 异常处理
│   ├── handler/           # 全局异常处理器
│   ├── interceptor/       # 拦截器（JWT、管理员认证）
│   ├── jwt/               # JWT 工具类
│   ├── Result.java        # 统一响应结果
│   └── UserRole.java      # 用户角色枚举
├── config/                # 配置类
│   ├── MetaObjectHandlerConfig.java
│   ├── MyBatisPlusConfig.java
│   ├── OssConfig.java
│   ├── RedisCacheConfig.java
│   ├── WebConfig.java
│   └── WebSocketConfiguration.java
├── controller/            # 控制器层
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── UserController.java
│   ├── CoachController.java
│   ├── GymStoreController.java
│   ├── EquipmentController.java
│   └── FitnessPlanController.java
├── dto/                   # 数据传输对象
├── entity/                # 实体类
├── mapper/                # MyBatis Mapper 接口
├── service/               # 服务层
│   ├── impl/             # 服务实现类
│   └── 各种 Service 接口
├── vo/                    # 视图对象
├── websocket/            # WebSocket 相关
└── GymratApplication.java
```

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/Springfield1-1-1/GymratRemoteRepository.git
cd gymrat
```

### 2. 配置数据库

创建 MySQL 数据库 `springfield_gymrat`，并导入相应的 SQL 脚本。

### 3. 配置 Redis

确保 Redis 服务已启动，默认配置为：

- Host: localhost
- Port: 6379
- Database: 1

### 4. 配置阿里云 OSS

在 `application.yml` 中配置阿里云 OSS 相关信息，或通过环境变量设置：

```yaml
aliyun:
  oss:
    access-key-id: ${ALIYUN_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_OSS_ACCESS_KEY_SECRET}
    endpoint: oss-cn-beijing.aliyuncs.com
    bucket-name: springfield-gymrat
```

### 5. 运行项目

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用 Maven
mvn spring-boot:run
```

项目将在 http://localhost:8080 启动。

### 6. 访问 API 文档

启动后访问 http://localhost:8080/swagger-ui.html

## 核心特性

### JWT 认证

系统使用 JWT 进行用户认证，Token 有效期支持：

- 短期 Token: 1 天
- 长期 Token: 30 天

### Redis 缓存

使用 Redis 缓存热点数据，提升系统性能：

- 器械列表缓存
- 门店列表缓存
- 门店详情缓存

### 文件存储

集成阿里云 OSS，支持：

- 用户头像上传
- 教练头像上传
- 门店图片上传
- 器械图片上传

### 全局异常处理

统一异常处理机制，返回标准化的错误响应。

## 开发规范

- 使用 Lombok 简化代码
- 遵循 RESTful API 设计规范
- 使用 DTO 进行参数传递，VO 进行数据返回
- 使用 @Validated 进行参数校验
- 日志记录使用 SLF4J + Lombok @Slf4j

## 监控与管理

集成 Spring Boot Admin，访问 http://localhost:8080 查看应用监控信息。

## 注意事项

1. 生产环境请修改 JWT 密钥
2. 需要配置阿里云 OSS 凭证
3. 数据库连接信息需根据实际环境配置
