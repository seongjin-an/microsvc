FROM openjdk:17-ea-17-jdk-slim
VOLUME /tmp
COPY target/dicoveryservice-1.0.jar DiscoveryService.jar
ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]