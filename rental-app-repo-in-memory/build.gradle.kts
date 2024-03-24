plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val cache4kVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":rental-app-model"))
    testImplementation(project(":rental-app-repo-tests"))
}
