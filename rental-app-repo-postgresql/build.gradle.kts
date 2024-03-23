plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val exposedVersion: String by project
    val testContainersVersion: String by project
    val postgresDriverVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":rental-app-model"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":rental-app-repo-tests"))
}
