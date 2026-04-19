# Use Java 21 (safe for Spring Boot 3)
FROM openjdk:21-jdk-slim

# App jar copy
COPY target/*.jar app.jar

# Run app
ENTRYPOINT ["java","-jar","/app.jar"]