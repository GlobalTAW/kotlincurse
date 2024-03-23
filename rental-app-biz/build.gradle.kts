plugins {
    kotlin("jvm")
}

dependencies {
    val coroutinesVersion: String by project

    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    implementation(kotlin("stdlib-common"))
    implementation(project(":rental-app-model"))
    implementation(project(":rental-app-stubs"))
    implementation(project(":ok-marketplace-lib-cor"))

    implementation(project(":rental-app-repo-tests"))
    implementation(project(":rental-app-repo-stubs"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))
}
