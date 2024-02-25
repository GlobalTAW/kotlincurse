rootProject.name = "rentalapp"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
    }
}

include("rental-app-api-v1")
include("rental-app-api-log")
include("rental-app-model")
include("rental-app-mappers-v1")
include("rental-app-mappers-log")
include("rental-app-spring")
include("rental-app-biz")
include("ok-marketplace-lib-cor")
include("rental-app-stubs")
include("rental-app-kafka")

include("rental-app-common")
include("rental-app-lib-logging")
include("rental-app-lib-logging-logback")
