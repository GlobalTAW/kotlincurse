plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("ru.teterin.rentalapp.kafka.MainKt")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(project(":rental-app-model"))
    implementation(project(":rental-app-api-v1"))
    implementation(project(":rental-app-mappers-v1"))
    implementation(project(":rental-app-biz"))

    testImplementation(kotlin("test-junit"))
}
