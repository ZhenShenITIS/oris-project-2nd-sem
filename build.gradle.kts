import java.util.*

plugins {
    id("java")
//    id("application")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    // плагин
    id("org.liquibase.gradle") version "2.2.2"

}

group = "itis"
version = "1.0-SNAPSHOT"

val springVersion: String by project
val jakartaVersion: String by project
val hibernateVersion: String by project
val postgresVersion: String by project
val freemarkerVersion: String by project
val hikariVersion: String by project
val springDataVersion: String by project
val lombokVersion: String by project
val logbackVersion: String by project
val jacksonVersion: String by project
val springSecurityVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework:spring-webmvc:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework:spring-context-support:$springVersion")
//    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")

    implementation("org.springframework.security:spring-security-taglibs")
    //providedCompile("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")

    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("org.springframework.boot:spring-boot-starter-security")
    //freemarker starter ->
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // javax mail ->
    implementation("javax.mail:javax.mail-api:1.6.2")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    // стартер для ликвидбейза ->
    implementation("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.33.0")
    liquibaseRuntime("org.postgresql:postgresql:$postgresVersion")
    // библиотека для работы с аргументами командной строки пикокли ->
    liquibaseRuntime("info.picocli:picocli:4.6.3")
}

tasks.test {
    useJUnitPlatform()
}

val props = Properties()
props.load(file("src/main/resources/db/liquibase.properties").inputStream())


liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to props.get("change-log-file"),
            "url" to props.get("url"),
            "username" to props.get("username"),
            "password" to props.get("password"),
            "driver" to props.get("driver-class-name")
        )
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

