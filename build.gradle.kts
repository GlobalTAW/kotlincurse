import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
}

val JVM_TARGET = "17"

group = "ru.teterin.rentalapp"
version = "1.0.0"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io")}
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }
    tasks.withType<KotlinJvmCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }

}
