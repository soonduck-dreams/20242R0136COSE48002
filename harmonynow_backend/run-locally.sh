#!/bin/bash

# 0. Gradle clean & build 실행
echo "Running './gradlew clean bootJar' to build the project..."
if ./gradlew clean bootJar; then
    echo "✅ Build completed successfully."
else
    echo "❌ Build failed. Please check the error logs."
    exit 1
fi

# 1. Docker Compose 이미지 빌드
echo "Building Docker images with 'docker compose build'..."
if docker compose build; then
    echo "✅ Docker images built successfully."
else
    echo "❌ Docker build failed. Please check the error logs."
    exit 1
fi

# 2. Docker Compose 컨테이너 재시작
echo "Starting containers with 'docker compose up --force-recreate'..."
if docker compose up --force-recreate -d; then
    echo "✅ Containers started successfully."
else
    echo "❌ Failed to start containers. Please check the error logs."
    exit 1
fi
