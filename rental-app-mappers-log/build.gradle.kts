plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    api(project(":rental-app-api-log"))
    implementation(project(":rental-app-model"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    testImplementation(kotlin("test-junit"))
}
