# Start with a base image containing Java runtime
FROM openjdk:15-alpine

# Add Author info
#LABEL maintainer="ycshin@medicalip.com"

# Add a volume to /tmp
VOLUME /home/login/springboot

# Make port 8080 available to the world outside this container
EXPOSE 9001

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} /home/login/springboot/login.jar

# Setting environment variables
# Note: this application relies on environment variables that contain secrets. Can pass along to image via:
# https://stackoverflow.com/questions/30494050/how-do-i-pass-environment-variables-to-docker-containers
ENV JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9001
# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/login/springboot/login.jar"]

