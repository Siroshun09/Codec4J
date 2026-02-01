package dev.siroshun.codec4j.io.gson;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.testhelper.io.FileIOTestCase;
import dev.siroshun.codec4j.testhelper.io.TextFileIOTest;
import org.junit.jupiter.api.Assertions;

import java.util.stream.Stream;

class GsonIOTest extends TextFileIOTest {

    @Override
    protected Stream<FileIO> implementations() {
        return Stream.of(GsonIO.DEFAULT, GsonIO.PRETTY_PRINTING);
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
