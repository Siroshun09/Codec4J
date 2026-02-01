package dev.siroshun.codec4j.io.base64;

import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.io.gson.GsonIO;
import dev.siroshun.codec4j.testhelper.io.FileIOTest;
import dev.siroshun.codec4j.testhelper.io.FileIOTestCase;

import java.util.stream.Stream;

class Base64IOTest extends FileIOTest {

    @Override
    protected Stream<FileIO> implementations() {
        return Stream.of(Base64IO.create(GsonIO.DEFAULT), Base64IO.createUrlBase64(GsonIO.DEFAULT));
    }

    @Override
    protected Stream<FileIOTestCase<?>> createTestCases(Stream<FileIO> impls) {
        return impls.flatMap(FileIOTestCase::forAllSources);
    }
}
