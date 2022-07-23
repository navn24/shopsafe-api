FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT java -Dspring.profiles.active=${SPRING_PROFILE} -jar /app.jar 
