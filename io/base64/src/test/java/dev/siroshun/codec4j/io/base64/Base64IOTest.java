package dev.siroshun.codec4j.io.base64;

import dev.siroshun.codec4j.io.gson.GsonIO;
import dev.siroshun.codec4j.testhelper.FileIOTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class Base64IOTest {

    @ParameterizedTest
    @MethodSource("testCasesForBase64")
    void testBase64(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForBase64() {
        return FileIOTestCase.forAllSources(Base64IO.create(GsonIO.DEFAULT));
    }

    @ParameterizedTest
    @MethodSource("testCasesForUrlBase64")
    void testUrlBase64(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForUrlBase64() {
        return FileIOTestCase.forAllSources(Base64IO.createUrlBase64(GsonIO.DEFAULT));
    }
}
