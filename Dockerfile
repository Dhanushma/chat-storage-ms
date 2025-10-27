FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

EXPOSE 8080

COPY target/chat-storage-ms-0.0.1-SNAPSHOT.jar chat-storage-ms-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "chat-storage-ms-0.0.1-SNAPSHOT.jar"]