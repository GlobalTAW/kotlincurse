plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project
    implementation(kotlin("stdlib-common"))

    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    api(project(":rental-app-lib-logging"))
    testImplementation(kotlin("test-junit"))
}
