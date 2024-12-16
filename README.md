<div align="center">
<img src="https://github.com/user-attachments/assets/1b53e737-6534-484f-a09e-3c143d2983e8" alt="HarmonyNow 로고 작은 버전">
<h1>HarmonyNow</h1>
<h4>화성학이 지금, 당신의 음악이 될 수 있도록</h4>
<h6>Capstone Design, 2024 Fall</h6>
</div>


# Project Description
**HarmonyNow는 아마추어와 프로 작곡가 모두가 원하는 코드 진행을 손쉽게 음악 창작에 활용할 수 있도록 돕는 작곡 어시스턴트 서비스입니다.**

작곡을 취미로 즐기는 사람들부터 전문 음악가들까지, 많은 이들이 화성학을 활용해 자신의 음악을 더욱 풍부하고 정교하게 표현하고자 합니다. 하지만 새로운 코드(chord)와 코드 진행(chord progression)을 익히고 이를 창작에 적용하려면, 흩어진 자료를 학습하고 각 코드의 느낌을 파악하는 데 많은 시간과 노력이 필요합니다. 이는 종종 아티스트의 창작 과정에 부담이 되기도 합니다. 

HarmonyNow 프로젝트는 이러한 문제를 해결하고 창작의 효율성을 높이고자 기획되었습니다. 이 서비스는 아마추어와 프로 작곡가들이 다양하고 실용적인 코드 및 코드 진행을 탐색하고, 음악 창작에 바로 활용할 수 있도록 지원합니다. 화성학 학습 콘텐츠와 symbolic music generation 기술을 기반으로, 사용자가 원하는 코드 진행을 음악 창작에 손쉽게 적용할 수 있는 도구를 제공합니다. HarmonyNow는 창작자들에게 영감을 제공하고, 복잡한 과정을 단순화하며, 창작의 장벽을 낮추는 든든한 지원군이 되는 것을 목표로 합니다.

# Features
### 음악 예시 생성
사용자가 원하는 코드 진행을 시작점으로 하는 음악 예시를 생성하는 기능입니다. 오디오와 함께 MIDI 파일을 제공하여, 생성된 음악을 사용자가 직접 자세히 들여다보고 편집할 수 있게 합니다. MIDI 파일은 음악을 구성하는 음이나 악기 정보 등을 가지고 있어, DAW(Digital Audio Workstation, 음악 편집 프로그램)를 사용해 편집하고 사용자가 음악을 창작하는 데 직접적으로 활용할 수 있습니다.

### 화성학 학습 콘텐츠 제공
다양하고 실용적인 코드와 코드 진행을 모아 놓았습니다. 각 코드와 코드 진행마다 어떤 소리가 나는지 들을 수 있고, 화성학적인 설명을 곁들여 사용자가 원하는 만큼 학습할 수 있습니다.

화성학적 기준에 따라 코드들의 난이도를 초급/중급/고급으로 분류하여 제공하고, 이 코드가 쓰이고 있는 코드 진행들을 비교해 듣거나, 이 코드 진행에 포함된 각 코드 소리를 따로 듣는 등 사용자가 원하는 대로 코드와 코드 진행을 오가며 학습할 수 있습니다. 

### 질문하기
화성학 학습 콘텐츠를 읽다가 모르는 용어나 궁금한 점 등을 채팅을 통해 물어볼 수 있는 기능입니다. gpt-4o-mini 모델이 현재 사용자가 위치한 웹 페이지 텍스트와 미리 준비된 화성학 도메인 지식을 참고하여 답변을 제공합니다.

### 관리자 페이지
관리자는 코드와 코드 진행을 조회/추가/수정/삭제할 수 있습니다.

# ER Diagram
![image](https://github.com/user-attachments/assets/ad267a88-9756-4d22-921d-dff3fd3d1e97)
코드(Chord) 정보를 저장하는 테이블, 코드 진행(Progression) 정보를 저장하는 테이블, 그리고 그 둘 간의 다대다 매핑을 위한 중간 테이블(ChordProgressionMap)로 구성하였습니다.

# System Architecture
![image](https://github.com/user-attachments/assets/7601d41d-efe2-4fa2-b267-12cd411b747c)
- 웹 서버 구성 요소(Spring Boot, PostgreSQL, redis)를 Docker Compose로 컨테이너화
- 모델 서버(FastAPI)는 개별 컨테이너화
- 웹 서버와 모델 서버 각각 GitHub 리포지토리로 형상 관리
- 모델 서버 리포지토리를 웹 서버 리포지토리에 submodule로 추가하여 웹 서버 리포지토리에서 모델 서버의 codebase도 함께 확인할 수 있게 관리

# Requirements
이 프로젝트를 설치하고 실행하기 위해서는 다음 프로그램들이 필요합니다.
- Java 17 이상
- Docker
- Docker Compose
- (Optional) Docker Desktop (Windows 환경에서 실행 시 필요할 수 있습니다)

# How to install and run
1. 이 리포지토리를 `git clone`합니다.
```
git clone https://github.com/soonduck-dreams/20242R0136COSE48002.git
```

2. harmonynow_backend 디렉토리로 이동하세요.
```
cd harmonynow_backend
```

3. 이 프로젝트는 실행에 필요한 환경 변수를 .env 파일로 관리합니다. 아래와 같이 `/harmonynow_backend` 디렉토리에 .env 파일을 생성하고 설정합니다.
`/harmonynow_backend/.env`:
```
# PostgreSQL 데이터베이스 설정
DB_NAME=your_database_name                    # 사용할 데이터베이스 이름
SPRING_DATASOURCE_URL=jdbc:postgresql://your_postgres_host:5432/your_database_name # 데이터베이스 연결 URL
SPRING_DATASOURCE_USERNAME=your_database_user    # 데이터베이스 사용자 이름
SPRING_DATASOURCE_PASSWORD=your_database_password # 데이터베이스 사용자 비밀번호

# Redis 설정
REDIS_HOST=your_redis_host                     # Redis 서버 호스트 이름 (컨테이너 내부 이름 사용 가능)
REDIS_PORT=6379                                # Redis 서버 포트 번호

# 파일 경로 설정
SOURCE_BASE_PATH=classpath:db/dummyfiles/      # 초기 데이터의 소스 경로
TARGET_BASE_PATH=/app/uploads                  # 업로드된 파일이 저장될 경로

# OpenAI API 설정
OPENAI_SECRET_KEY=your_openai_secret_key       # OpenAI API의 비밀 키
OPENAI_URL=https://api.openai.com/v1/chat/completions # OpenAI API의 기본 URL

# 음악 생성 모델 서버 URL
MODEL_SERVER_URL=http://your_model_server_url:8000 # 모델 서버의 엔드포인트

# 관리자 코드
ADMIN_CODE=your_admin_code                     # 애플리케이션에서 사용할 관리자 코드

# 배포 서버 IP
DEPLOYMENT_SERVER_HOST_IP=your_deployment_server_ip # 배포 서버의 IP 주소
```

A. 다음 명령어를 실행하여 애플리케이션을 로컬에서 실행합니다:
```
./run-locally.sh
```
- 이 명령어는 Gradle 빌드, Docker Compose 빌드 및 컨테이너 실행을 자동으로 처리합니다.
- 실행 후, 애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.

B-1. 다음 명령어를 실행하여 애플리케이션을 실행하기 위해 필요한 파일들을 원격 서버에 배포합니다.
```
./prepare-deployment.sh
```
- 배포 전에 .env 파일에서 DEPLOYMENT_SERVER_HOST_IP 값이 올바르게 설정되어 있는지 확인하세요.
- 원격 서버에 대한 SSH 인증이 필요할 수 있습니다.

B-2. `./prepare-deployment.sh`로 파일들을 배포한 후, 원격 서버에서 애플리케이션을 실행하려면 다음 명령어를 사용합니다:
```
docker compose up
```

# Development Environment
- OS: Windows 11(로컬 개발 환경), CentOS 7.8(배포 환경, Naver Cloud Platform)
- IDE: IntelliJ IDEA
- DB: PostgreSQL
- Framework & Libraries
  - Spring Boot ─ 웹 애플리케이션 프레임워크
  - Thymeleaf ─ 서버사이드 템플릿 엔진
  - JPA ─ 데이터베이스 ORM 관리
  - Redis ─ 인메모리 데이터 스토리지 (모델 서버로부터 받은 음악 파일 캐시)
  - Flyway ─ SQL 버전 관리, DB 생성 및 초기 데이터 삽입 자동화
 
- 모델 서버의 개발 환경은 `/model/README.md`에서 확인할 수 있습니다.

# Directory Structure
```
project-root/
├── model/  # 음악 생성용 모델 서버 repository. git submodule로 최신 commit 참조.
└── harmonynow_backend/
    ├── src/
    │   └── main/
    │       ├── java/
    │       │   └── jhhan/harmonynow_backend/
    │       │       ├── chat/          # 채팅(질문하기) 기능 관련
    │       │       ├── config/        # 설정 관련 코드
    │       │       ├── controller/    # 컨트롤러 계층
    │       │       ├── domain/        # 도메인 모델
    │       │       ├── dto/           # 데이터 전송 객체
    │       │       ├── exception/     # 예외 처리
    │       │       ├── repository/    # 데이터베이스 접근 계층
    │       │       ├── service/       # 비즈니스 로직 계층
    │       │       ├── utils/         # 유틸리티 코드 (파일 입출력)
    │       │       └── validation/    # 폼 데이터 검증
    │       └── resources/
    │           ├── db/
    │           │   ├── dummyfiles/     # 개발 및 시연용 초기 데이터
    │           │   │   ├── chord/     # 코드(chord)의 소리와 건반 위치 이미지 (audio/, image/)
    │           │   │   └── progression/  # 코드 진행(progression)의 소리와 MIDI 샘플 (audio/, sample_midi/)
    │           │   └── migration/     # 데이터베이스 마이그레이션 파일 (Flyway 기반 SQL 실행)
    │           ├── static/            # 정적 파일 (css/, image/, js/)
    │           └── templates/         # HTML 템플릿
    ├── Dockerfile                     # Spring Boot 이미지 빌드 파일 (로컬 개발용)
    ├── Dockerfile.remote              # Spring Boot 이미지 빌드 파일 (배포 환경용)
    ├── docker-compose.yml             # 컨테이너(Web, Redis, PostgreSQL) 구성
    ├── docker-compose.override.yml    # 로컬 개발용 Docker Compose override 정의
    ├── run-locally.sh                 # 로컬 개발 환경 실행 스크립트 (JAR 빌드 및 컨테이너 시작)
    ├── prepare-deployment.sh          # 배포 스크립트 (JAR 빌드, 파일 준비 및 서버 전송)
    ├── build.gradle                   # 프로젝트 빌드 설정 파일 
    │                                   # (프레임워크 및 라이브러리 정의, 종속성 관리)
    └── ... (기타 설정 파일)
```

# Demonstration Video
to be added

# License
to be added
