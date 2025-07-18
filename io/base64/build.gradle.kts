plugins {
    alias(libs.plugins.aggregated.javadoc.collector)
    alias(libs.plugins.mavenPublication)
}

dependencies {
    api(projects.codec4jApi)

    testImplementation(projects.codec4jIoGson)
    testImplementation(projects.codec4jTestHelper)
    testImplementation(libs.jfun.result.assertion)
}
