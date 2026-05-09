# TextShare

A LAN-based text sharing application. Share text via generated links that expire after 24 hours.

[中文文档](doc/README_zh.md)

## Features

- Share text via unique generated links
- Links expire after 24 hours
- View count tracking
- Multi-tenancy support
- Rate limiting
- Dark mode support

## Tech Stack

- **Backend**: Spring Boot 3.2.5 (Java 21), PostgreSQL, Redis
- **Frontend**: Vue 3, Vite, TypeScript, Tailwind CSS

## Quick Start

### Backend

```bash
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Docker

```bash
DB_PASSWORD=xxx REDIS_PASSWORD=xxx docker-compose up -d
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/texts` | Create new text share |
| GET | `/api/v1/texts/{id}` | Retrieve text by ID |
| POST | `/api/v1/texts/{id}/view` | Increment view count |
| GET | `/api/v1/texts/{id}/view` | Get current view count |
| DELETE | `/api/v1/texts/{id}` | Delete text |

## Project Structure

```
├── src/main/java/com/textshare/     # Spring Boot backend
│   ├── controller/                   # REST API endpoints
│   ├── service/                      # Business logic
│   ├── repository/                   # JPA repositories
│   ├── entity/                       # JPA entities
│   └── ...
├── frontend/                        # Vue 3 frontend
├── docker/                         # Docker configuration
├── docker-compose.yml              # PostgreSQL + Redis + app
└── init.sql                         # Database schema
```

## License

MIT
