plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    implementation(kotlin("stdlib-common"))
    implementation(project(":rental-app-model"))
    implementation(project(":rental-app-stubs"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))
}
