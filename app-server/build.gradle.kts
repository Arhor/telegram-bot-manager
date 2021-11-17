@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Paths

plugins {
    alias(libs.plugins.flyway)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring)
}

group = "dev.arhor.telegram"
description = "telegram-bot-manager-server"

val javaVersion = libs.versions.java.get()

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${libs.versions.testcontainers.get()}")
    }
}

configurations {
    testImplementation {
        exclude(module = "junit-vintage-engine")
    }
}

dependencies {
    kapt(libs.mapstruct.processor)
    kapt(libs.spring.context.indexer)

    compileOnly(libs.mapstruct)

    runtimeOnly(libs.postgresql)

    developmentOnly(libs.spring.boot.devtools)

    implementation("de.codecentric:spring-boot-admin-starter-client:2.5.3")
    implementation("de.codecentric:spring-boot-admin-starter-server:2.5.3")
    implementation("de.codecentric:spring-boot-admin-server-ui:2.5.4")
//    implementation("de.codecentric:spring-boot-admin-server-ui-login:1.5.7")

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.flyway.core)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jdbc)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.telegrambots)
    implementation(libs.telegrambots.extensions)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
}

tasks {
    val copyLatestClientBuild = register<Copy>("copyLatestClientBuild") {
        // TODO: make it smarter - calc checksum, copy if present and newer
        val clientPrjDir = project(":app-client").projectDir.toString()
        val serverBldDir = project(":app-server").buildDir.toString()

        from(Paths.get(clientPrjDir, "dist"))
        into(Paths.get(serverBldDir, "resources", "main", "static"))
    }

    processResources {
        dependsOn(copyLatestClientBuild)
    }


    bootRun {
        jvmArgs = listOf(
            "-XX:+UseSerialGC",
            "-XX:MaxRAM=100m",
            "-Xss512k",
        )
    }

    flyway {
        // TODO: get variables from real env or .env file
        url = System.getenv("JDBC_DATABASE_URL")
        user = System.getenv("JDBC_DATABASE_USERNAME")
        password = System.getenv("JDBC_DATABASE_PASSWORD")

        driver = "org.postgresql.Driver"
        encoding = "UTF-8"
        locations = arrayOf("classpath:db/migration")
        baselineVersion = "0.0"
        baselineOnMigrate = true
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

//    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
//        exclude { developmentOnly.resolvedConfiguration.files.contains(it.file) }
//    }
}
