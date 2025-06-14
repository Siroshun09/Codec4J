package dev.siroshun.codec4j.io.gzip;

import dev.siroshun.codec4j.io.gson.GsonIO;
import dev.siroshun.codec4j.testhelper.FileIOTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.Deflater;

class GzipIOTest {

    @ParameterizedTest
    @MethodSource("testCasesForNoCompression")
    void testNoCompression(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForNoCompression() {
        return FileIOTestCase.forAllSources(GzipIO.noCompression(GsonIO.DEFAULT));
    }

    @ParameterizedTest
    @MethodSource("testCasesForDefaultCompression")
    void testDefaultCompression(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForDefaultCompression() {
        return FileIOTestCase.forAllSources(GzipIO.defaultCompression(GsonIO.DEFAULT));
    }

    @ParameterizedTest
    @MethodSource("testCasesForBestCompression")
    void testBestCompression(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForBestCompression() {
        return FileIOTestCase.forAllSources(GzipIO.bestCompression(GsonIO.DEFAULT));
    }

    @ParameterizedTest
    @MethodSource("testCasesForCustomCompression")
    void testCustomCompressionLevel(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForCustomCompression() {
        return IntStream.rangeClosed(0, 9).filter(level ->
            // already tested
            level != Deflater.NO_COMPRESSION &&
            level != Deflater.BEST_COMPRESSION
        ).boxed().flatMap(level -> FileIOTestCase.forAllSources(GzipIO.create(GsonIO.DEFAULT, level)));
    }
}
