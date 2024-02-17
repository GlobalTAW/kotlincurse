plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val logbackVersion: String by project
    val logbackAppendersVersion: String by project
    val logbackEncoderVersion: String by project
    val fluentLoggerVersion: String by project
    val coroutinesVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":rental-app-lib-logging"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    // logback
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    api("com.sndyuk:logback-more-appenders:$logbackAppendersVersion")
    api("org.fluentd:fluent-logger:$fluentLoggerVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(kotlin("test-junit"))
}
