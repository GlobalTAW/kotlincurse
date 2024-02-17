plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":rental-app-model"))
    implementation(project(":rental-app-mappers-v1"))
    implementation(project(":rental-app-mappers-log"))
    implementation(project(":rental-app-stubs"))
    implementation(project(":rental-app-biz"))
}
