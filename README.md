# 🔐 Login API 서비스

본 저장소는 로그인 및 사용자 인증을 처리하는 REST API 서비스입니다.  
Spring Boot와 Spring Security를 기반으로 개발되었으며, Docker 컨테이너화를 지원합니다.

## 🧩 프로젝트 구성

```
login/
├── .jpb/                 # JPA Buddy 설정
├── .mvn/wrapper/         # Maven Wrapper 설정
├── src/                  # 소스 코드
├── .gitignore           # Git 무시 파일 설정
├── .gitlab-ci.yml       # GitLab CI/CD 파이프라인 설정
├── Dockerfile           # Docker 이미지 빌드 설정
├── docker-compose.yml   # Docker Compose 설정
├── mvnw                 # Maven Wrapper 스크립트 (Unix)
├── mvnw.cmd             # Maven Wrapper 스크립트 (Windows)
└── pom.xml              # Maven 프로젝트 설정
```

## 🛠 사용 기술

* Java 11+
* Spring Boot
* Spring Security
* Spring Data JPA
* Docker & Docker Compose
* GitLab CI/CD
* Maven 3.6+

## 🚀 시작하기

### 1. 로컬 환경에서 실행
```bash
# 저장소 클론
git clone https://github.com/yongchulShin/login.git
cd login

# 프로젝트 빌드
./mvnw clean install

# 애플리케이션 실행
./mvnw spring-boot:run
```

### 2. Docker로 실행
```bash
# Docker 이미지 빌드
docker build -t login-api .

# Docker Compose로 실행
docker-compose up -d
```

## ⚡ 주요 기능

* **사용자 관리**
  - 회원가입
  - 로그인/로그아웃
  - 사용자 정보 관리

* **인증/인가**
  - JWT 기반 인증
  - 권한 관리
  - 토큰 갱신

* **보안**
  - 비밀번호 암호화
  - CORS 설정
  - XSS 방지

## 🔄 CI/CD

GitLab CI/CD 파이프라인이 구성되어 있으며, 다음 단계로 자동화되어 있습니다:

* 빌드 및 테스트
* Docker 이미지 생성
* 품질 검사 (SonarQube)
* 자동 배포

## 📋 API 문서

API 문서는 Swagger UI를 통해 제공됩니다:
```
http://localhost:8080/swagger-ui.html
```

## ⚙️ 개발 환경

프로젝트는 다음 프로파일을 지원합니다:
* `local`: 로컬 개발 환경
* `dev`: 개발 서버 환경
* `prod`: 운영 서버 환경

## 🙋‍♂️ About Me

신용철 (Yongchul Shin)  
전 서든어택 프로게이머 → 백엔드 개발자  
GitHub: github.com/yongchulShin  
Email: prozernim@gmail.com 
