FROM openjdk:17-ea-17-jdk-slim
VOLUME /tmp
COPY target/user-service2-1.0.jar UserService.jar
ENTRYPOINT ["java", "-jar", "UserService.jar"]