FROM gradle:8.0-jdk21 AS builder

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

# Этап 2: Финальный образ
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
