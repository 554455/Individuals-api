
FROM gradle:8.5-jdk21 AS builder

COPY . /app
COPY src/libs/common/0.0.1-SNAPSHOT/common-0.0.1-SNAPSHOT-plain.jar /app/src/libs/common/0.0.1-SNAPSHOT/common-0.0.1-SNAPSHOT-plain.jar
# Устанавливаем рабочую директорию
WORKDIR /app

RUN ./gradlew build -x test

# Этап 2: Финальный образ
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

ENV DEBUG_INFO="-Xdebug -Xrunjdwp:transport=dt_socket,address=*:5005,server=y,suspend=n"
ENTRYPOINT [ "sh", "-c", "java ${DEBUG_INFO} -Dspring.profiles.active=docker",  "-jar", "app.jar"]
