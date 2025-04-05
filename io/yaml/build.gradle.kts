plugins {
    alias(libs.plugins.aggregated.javadoc.collector)
    alias(libs.plugins.mavenPublication)
}

dependencies {
    api(projects.codec4jApi)
    api(libs.snakeyaml)

    testImplementation(projects.codec4jTestHelper)
}
