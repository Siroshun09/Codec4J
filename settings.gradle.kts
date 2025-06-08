pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "codec4j"

addProject("api")
addProject("test-helper")

sequenceOf(
    "gson",
    "gzip",
    "memory",
    "yaml"
).forEach {
    addProject("io-$it", "./io/$it")
}

fun addProject(name: String) {
    addProject(name, name)
}

fun addProject(name: String, dir: String) {
    include("${rootProject.name}-$name")
    project(":${rootProject.name}-$name").projectDir = file(dir)
}
