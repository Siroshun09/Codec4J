plugins {
    alias(libs.plugins.aggregated.javadoc.collector)
    alias(libs.plugins.mavenPublication)
}

dependencies {
    testImplementation(projects.codec4jIoMemory)
    testImplementation(projects.codec4jTestHelper)
}
