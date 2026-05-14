# TextShare

局域网文本分享应用。通过生成的链接分享文本，链接24小时后自动过期。

[English Documentation](../README.md)

## 功能特点

- 通过唯一生成的链接分享文本
- 链接24小时后自动过期
- 浏览量统计
- 多租户支持
- 限流保护
- 深色模式支持
- 国际化支持（中文/英文）
- XSS防护（输出编码）

## 技术栈

- **后端**: Spring Boot 3.2.5 (Java 21), PostgreSQL, Redis
- **前端**: Vue 3, Vite, TypeScript, Tailwind CSS

## 快速开始

### 后端启动

```bash
mvn spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

### Docker 部署

```bash
DB_PASSWORD=xxx REDIS_PASSWORD=xxx docker-compose up -d
```

### 环境变量

| 变量 | 描述 |
|------|------|
| `DB_PASSWORD` | PostgreSQL 密码 |
| `REDIS_PASSWORD` | Redis 密码 |
| `CORS_ALLOWED_ORIGINS` | CORS 允许的来源（逗号分隔） |

## API 接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/v1/texts` | 创建文本分享 |
| GET | `/api/v1/texts/{id}` | 获取文本内容 |
| POST | `/api/v1/texts/{id}/view` | 增加浏览次数 |
| GET | `/api/v1/texts/{id}/view` | 获取浏览次数 |
| DELETE | `/api/v1/texts/{id}` | 删除文本 |

## 项目结构

```
├── src/main/java/com/textshare/     # Spring Boot 后端
│   ├── controller/                   # REST API 控制器
│   ├── service/                      # 业务逻辑
│   ├── repository/                   # JPA 仓库
│   ├── entity/                       # JPA 实体
│   └── ...
├── frontend/                        # Vue 3 前端
├── docker/                          # Docker 配置
├── docker-compose.yml               # PostgreSQL + Redis + 应用
└── init.sql                         # 数据库架构
```

## 开源协议

MIT