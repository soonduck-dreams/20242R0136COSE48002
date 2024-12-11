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

# How to install and run
to be added

# Development Environment
to be added

# Directory Structure
to be added

# Demonstration Video
to be added

# License
to be added
