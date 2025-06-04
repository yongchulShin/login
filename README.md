# Login API

로그인 및 사용자 인증을 처리하는 REST API 서비스입니다. Spring Boot 기반으로 개발되었으며, Docker 컨테이너화를 지원합니다.

## 기술 스택

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Docker
- GitLab CI/CD
- Maven

## 프로젝트 구조

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

## 시작하기

### 필수 조건

- JDK 11 이상
- Maven 3.6 이상
- Docker & Docker Compose (컨테이너 실행 시 필요)

### 로컬 환경에서 실행

1. 레포지토리 클론
```bash
git clone https://github.com/yongchulShin/login.git
```

2. 프로젝트 디렉토리로 이동
```bash
cd login
```

3. 프로젝트 빌드
```bash
./mvnw clean install
```

4. 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

### Docker로 실행

1. Docker 이미지 빌드
```bash
docker build -t login-api .
```

2. Docker Compose로 실행
```bash
docker-compose up -d
```

## API 문서

API 문서는 Swagger UI를 통해 확인할 수 있습니다.
- 로컬 환경: http://localhost:8080/swagger-ui.html

## CI/CD

GitLab CI/CD 파이프라인이 구성되어 있으며, 다음과 같은 단계로 자동화되어 있습니다:
- 빌드
- 테스트
- Docker 이미지 생성
- 배포

## 주요 기능

- 사용자 회원가입
- 로그인/로그아웃
- JWT 기반 인증
- 사용자 정보 관리
- 권한 관리

## 개발 환경 설정

프로젝트는 다음과 같은 프로파일을 지원합니다:
- local: 로컬 개발 환경
- dev: 개발 서버 환경
- prod: 운영 서버 환경

## 라이선스

This project is licensed under the MIT License - see the LICENSE file for details.
