plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":rental-app-model"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))
}
