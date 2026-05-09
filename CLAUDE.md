# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TextShare is a LAN-based text sharing application with a Spring Boot backend and Vue 3 frontend. Users share text via generated links that expire after 24 hours.

## Architecture

```
├── src/main/java/com/textshare/     # Spring Boot backend
│   ├── controller/                   # REST API endpoints (/api/v1/texts)
│   ├── service/                      # Business logic (TextService, RateLimitService, AuditLogService)
│   ├── repository/                   # JPA repositories (Text, AccessLog, Tenant)
│   ├── entity/                       # JPA entities
│   ├── dto/                          # Request/Response DTOs
│   ├── filter/                       # TenantInterceptor, RateLimitFilter, LoggingFilter
│   ├── config/                       # Redis, Web configuration
│   ├── scheduler/                    # CleanupScheduler (expires texts)
│   ├── exception/                    # Global exception handling
│   └── util/                         # IdGenerator, HtmlEscapeUtil
├── frontend/                        # Vue 3 + Vite + TypeScript frontend
│   └── src/composables/useApi.ts    # API calls to backend
├── docker/                         # Docker configuration
│   └── nginx.conf                   # Nginx config for production
├── docker-compose.yml              # PostgreSQL 15 + Redis 7 + app
└── init.sql                         # Database schema initialization
```

## Tech Stack

**Backend:**
- Spring Boot 3.2.5 (Java 21)
- Spring Data JPA with PostgreSQL
- Spring Data Redis (commons-pool2)
- Scheduled cleanup for expired texts

**Frontend:**
- Vue 3 with Composition API (`<script setup>`)
- Vite build tooling
- TypeScript
- Tailwind CSS (amber primary, dark mode via `.dark` class)
- Vue Router

## Commands

### Backend (from repo root)
```bash
mvn spring-boot:run    # Start backend dev server (port 8080)
mvn test               # Run tests
mvn package            # Build JAR
```

### Frontend (from frontend/ directory)
```bash
npm run dev      # Start Vite dev server (port 5173)
npm run build    # Type check + build for production
npm run preview  # Preview production build
```

### Docker
```bash
# Start all services (PostgreSQL + Redis + app with nginx on port 80)
DB_PASSWORD=xxx REDIS_PASSWORD=xxx docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/texts` | Create new text share |
| GET | `/api/v1/texts/{id}` | Retrieve text by ID |
| POST | `/api/v1/texts/{id}/view` | Increment view count |
| GET | `/api/v1/texts/{id}/view` | Get current view count |
| DELETE | `/api/v1/texts/{id}` | Delete text |

**Note:** In production (Docker), all API requests are proxied through nginx on port 80 to the backend on port 8080.

## Key Implementation Details

- **ID generation**: Uses custom `IdGenerator` (not UUID strings)
- **Expiry**: Texts expire after 24 hours; `CleanupScheduler` runs periodically
- **Rate limiting**: Per-IP rate limiting via `RateLimitService` backed by Redis
- **Multi-tenancy**: `TenantInterceptor` extracts tenant from request header `X-Tenant-Code`
- **Security**: HTML escaping via `HtmlEscapeUtil` before storage
- **Audit logging**: All access logged to `access_logs` table via `AuditLogService`
- **View count**: Uses Redis for real-time count, syncs to PostgreSQL on each increment
- **Docker production**: Single image with nginx (port 80) proxying to backend (port 8080)

## Database Schema

- `texts`: id (VARCHAR 32), content, ip_address, user_agent, device_info, view_count, expires_at, created_at, updated_at, is_deleted
- `access_logs`: id (BIGSERIAL), text_id, ip_address, user_agent, access_time, action
- `tenants`: id (UUID), code, name, status, settings (JSONB), created_at, updated_at

## Frontend Routes

| Path | Component | Description |
|------|-----------|-------------|
| `/` | SendView | Create and share text |
| `/s/:id` | ReceiveView | View shared text |
| `/expired` | ExpiredView | Expired link page |

## Environment Variables

Backend uses Spring profiles. Required for Docker:
- `DB_PASSWORD` - PostgreSQL password
- `REDIS_PASSWORD` - Redis password
- `SPRING_PROFILES_ACTIVE=prod` (set automatically in Docker)

For local development:
- `DB_HOST`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PASSWORD`
- `SPRING_PROFILES_ACTIVE`