package dev.siroshun.codec4j.io.gson;

import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.testhelper.io.FileIOTestCase;
import dev.siroshun.codec4j.testhelper.io.TextFileIOTest;

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
}
