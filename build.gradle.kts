import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("io.ktor.plugin") version "2.1.0"
}

group = "ru.ezhov"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.ezhov.friendassistant.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("friend-assistant.jar")
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("net.java.dev.jna:jna:5.7.0")
    implementation("com.alphacephei:vosk:0.3.38")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")

    implementation("org.slf4j:slf4j-simple:1.7.36")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.javaParameters = true
}