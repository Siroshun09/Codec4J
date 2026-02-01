package dev.siroshun.codec4j.io.gzip;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.io.gson.GsonIO;
import dev.siroshun.codec4j.testhelper.io.FileIOTest;
import dev.siroshun.codec4j.testhelper.io.FileIOTestCase;
import org.junit.jupiter.api.Assertions;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.Deflater;

class GzipIOTest extends FileIOTest {

    @Override
    protected Stream<FileIO> implementations() {
        return Stream.concat(
            Stream.of(GzipIO.noCompression(GsonIO.DEFAULT), GzipIO.defaultCompression(GsonIO.DEFAULT), GzipIO.bestCompression(GsonIO.DEFAULT)),
            IntStream.rangeClosed(0, 9).filter(level ->
                // already tested
                level != Deflater.NO_COMPRESSION &&
                level != Deflater.BEST_COMPRESSION
            ).boxed().map(level -> GzipIO.create(GsonIO.DEFAULT, level))
        );
    }

    @Override
    protected Stream<FileIOTestCase<?>> createTestCases(Stream<FileIO> impls) {
        return impls.flatMap(FileIOTestCase::forAllSources);
    }

    @Override
    protected void assertEncodeFailure(EncodeError expected, EncodeError error) {
        Assertions.assertTrue(
            Assertions.assertInstanceOf(EncodeError.MultipleError.class, error).errors().contains(expected)
        );
    }
}
