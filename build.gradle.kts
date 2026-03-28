plugins {
    id("java")
//    id("application")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"

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
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}

