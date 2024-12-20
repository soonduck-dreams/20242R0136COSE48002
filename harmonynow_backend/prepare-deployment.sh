#!/bin/bash

# .env 파일 로드
if [ -f ".env" ]; then
    export $(cat .env | xargs)
fi

# 변수 정의
PROJECT_ROOT=$(pwd)
DEPLOYMENT_SERVER_HOST_IP=${DEPLOYMENT_SERVER_HOST_IP}
DEPLOY_DIR="$PROJECT_ROOT/deployment"
BUILD_DIR="$PROJECT_ROOT/build/libs"
FILES=("Dockerfile.remote" "docker-compose.yml" ".env")

echo "DEPLOYMENT_SERVER_HOST_IP: $DEPLOYMENT_SERVER_HOST_IP"

# 0. Gradle clean & build 실행
echo "Running './gradlew clean bootJar' to build the project..."
if ./gradlew clean bootJar; then
    echo "✅ Build completed successfully."
else
    echo "❌ Build failed. Please check the error logs."
    exit 1
fi

# 1. deployment 디렉토리 생성 (존재하지 않으면)
if [ ! -d "$DEPLOY_DIR" ]; then
    echo "Creating deployment directory..."
    mkdir -p "$DEPLOY_DIR"
fi

# 2. 최신 JAR 파일 복사
echo "Looking for JAR files in $BUILD_DIR..."
JAR_FILE=$(ls -t $BUILD_DIR/*.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ No JAR file found in $BUILD_DIR. Please build the project first."
else
    echo "Copying JAR file: $(basename $JAR_FILE) to deployment directory..."
    cp "$JAR_FILE" "$DEPLOY_DIR/"
fi

# 3. Dockerfile, docker-compose.yml, .env 복사
for FILE in "${FILES[@]}"; do
    if [ -f "$PROJECT_ROOT/$FILE" ]; then
        echo "Copying $FILE to deployment directory..."
        cp "$PROJECT_ROOT/$FILE" "$DEPLOY_DIR/"
    else
        echo "⚠️  $FILE not found in project root. Skipping..."
    fi
done

# 4. ssh로 파일 전송 (원격 서버 인증 필요)
scp -r ./deployment root@$DEPLOYMENT_SERVER_HOST_IP:/home/web_server

