plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":rental-app-api-v1"))
    implementation(project(":rental-app-model"))

    testImplementation(kotlin("test-junit"))
}
