# 컨테이너를 구동시킬 자바를 받음
FROM openjdk:11-jdk-alpine

# JAR_FILE 이라는 변수로 jar파일 지정
ARG JAR_FILE=/volume1/docker/cafeapi/cafe-0.0.1-SNAPSHOT.jar

# 지정한 jar 파일을 cafe-api.jar 라는 이름으로 Docker Container에 추가
ADD ${JAR_FILE} cafe-api.jar

# cafe-api 파일을 실행
ENTRYPOINT [ "java", "-jar", "/cafe-api.jar" ]