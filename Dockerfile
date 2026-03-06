# syntax=docker/dockerfile:1
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./

COPY gradle/ ./gradle/
COPY gradle.properties ./

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

COPY src ./src
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle build -x test --no-daemon

RUN mkdir -p /app/exploded && \
    cd /app/exploded && \
    jar -xf /app/build/libs/*.war

FROM tomcat:10.1-jdk21
WORKDIR /usr/local/tomcat

COPY --from=builder /app/exploded/ webapps/ROOT/


EXPOSE 8080
CMD ["catalina.sh", "run"]
