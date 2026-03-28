# syntax=docker/dockerfile:1
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle/ ./gradle/

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

COPY src ./src

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle build -x test --no-daemon

# 1. Переименовываем JAR-файл перед распаковкой, чтобы не зависеть от версии (1.0-SNAPSHOT)
RUN cp build/libs/*.jar application.jar && \
    java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# --- Runtime Stage ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# 2. Копируем слои нового формата
COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"
EXPOSE 8080

# 3. Запускаем "тонкий" JAR вместо JarLauncher
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]