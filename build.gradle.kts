plugins {
    alias(libs.plugins.jcommon)
    alias(libs.plugins.aggregated.javadoc)
    alias(libs.plugins.mavenPublication)
    alias(libs.plugins.mavenCentralPortal)
}

jcommon {
    javaVersion = JavaVersion.VERSION_21

    commonDependencies {
        compileOnlyApi(libs.annotations)
        api(libs.jfun.function)
        api(libs.jfun.result)

        testImplementation(platform(libs.junit.bom))
        testImplementation(libs.junit.jupiter)
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation(libs.jfun.result.assertion)
    }
}

aggregatedJavadoc {
    modules = listOf("org.jetbrains.annotations", "org.junit.jupiter.api")
}

mavenPublication {
    localRepository(mavenCentralPortal.stagingDirectory)
    description("A library that provides the simple type-safe codec system.")
    apacheLicense()
    developer("Siroshun09")
    github("Siroshun09/Codec4J")
}
