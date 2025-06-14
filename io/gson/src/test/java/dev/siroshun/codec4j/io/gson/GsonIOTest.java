package dev.siroshun.codec4j.io.gson;

import dev.siroshun.codec4j.testhelper.FileIOTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class GsonIOTest {

    @ParameterizedTest
    @MethodSource("testCasesForDefault")
    void testDefault(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForDefault() {
        return FileIOTestCase.forAllSources(GsonIO.DEFAULT);
    }

    @ParameterizedTest
    @MethodSource("testCasesForPrettyPrinting")
    void testPrettyPrinting(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForPrettyPrinting() {
        return FileIOTestCase.forAllSources(GsonIO.PRETTY_PRINTING);
    }
}
