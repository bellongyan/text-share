# Stage 1: Build frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build backend
FROM eclipse-temurin:21-jdk-alpine AS backend-builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN apk add --no-cache maven && mvn package -DskipTests

# Stage 3: Final image with nginx
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Install nginx
RUN apk add --no-cache nginx

# Copy backend JAR
COPY --from=backend-builder /app/target/*.jar app.jar

# Copy nginx config
COPY docker/nginx.conf /etc/nginx/http.d/default.conf

# Copy frontend build
COPY --from=frontend-builder /app/frontend/dist /usr/share/nginx/html

# Expose only port 80
EXPOSE 80

# Start nginx in background and java application
CMD ["sh", "-c", "nginx -g 'daemon off;' & java -jar app.jar"]