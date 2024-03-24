plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val kotestVersion: String by project
    val springdocOpenapiUiVersion: String by project
    val coroutinesVersion: String by project
    val serializationVersion: String by project
    val springmockkVersion: String by project

    implementation("org.springframework.boot:spring-boot-starter-actuator") // info; refresh; springMvc output
    implementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springdocOpenapiUiVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // from models to json and Vice versa
    implementation("org.jetbrains.kotlin:kotlin-reflect") // for spring-boot app
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // for spring-boot app
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // Внутренние модели
    implementation(project(":rental-app-model"))
    implementation(project(":rental-app-common"))

    // v1 api
    implementation(project(":rental-app-api-v1"))
    implementation(project(":rental-app-mappers-v1"))

    // biz
    implementation(project(":rental-app-biz"))
    implementation(project(":rental-app-lib-logging-logback"))

    //repo
    implementation(project(":rental-app-repo-postgresql"))
    implementation(project(":rental-app-repo-in-memory"))
    implementation(project(":rental-app-repo-stubs"))

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")

//    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux") // Controller, Service, etc..
}

tasks {
    withType<ProcessResources> {
        from("$rootDir/specs") {
            into("/static")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
