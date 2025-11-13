import org.springframework.boot.gradle.tasks.bundling.BootJar

//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//
//plugins {
//    kotlin("jvm") version "1.6.21"
//    id("io.ktor.plugin") version "2.1.0"
//}
//
//group = "ru.ezhov"
//version = "1.0-SNAPSHOT"
//
//application {
//    mainClass.set("ru.ezhov.friendassistant.AppKt")
//}
//
//ktor {
//    fatJar {
//        archiveFileName.set("friend-assistant.jar")
//    }
//}
//
//repositories {
//    mavenCentral()
//    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
//}
//
//dependencies {
//    implementation("net.java.dev.jna:jna:5.7.0")
//    implementation("com.alphacephei:vosk:0.3.38")
//
//    implementation("org.apache.httpcomponents:httpclient:4.5.13")
//
//    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
//
//    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")
//
//    implementation("org.slf4j:slf4j-simple:1.7.36")
//
//    testImplementation(kotlin("test"))
//}
//
//tasks.test {
//    useJUnitPlatform()
//}
//
//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//    kotlinOptions.javaParameters = true
//}


plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.ezhov"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
    implementation("net.java.dev.jna:jna:5.7.0")
    implementation("com.alphacephei:vosk:0.3.38")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootJar> {
    archiveFileName = "friend-assistant.jar"
}
