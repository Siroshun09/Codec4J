package dev.siroshun.codec4j.testhelper.io;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.testhelper.codec.ErrorCodec;
import dev.siroshun.codec4j.testhelper.source.MapSource;
import dev.siroshun.codec4j.testhelper.source.ValueSource;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class FileIOTest {

    @ParameterizedTest
    @MethodSource("testCases")
    protected <T> void encodeAndDecodeStream(FileIOTestCase<T> testCase) {
        FileIO io = testCase.io();
        T value = testCase.value();
        Codec<T> codec = testCase.codec();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ResultAssertions.assertSuccess(io.encodeTo(out, codec, value));

            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                ResultAssertions.assertSuccess(io.decodeFrom(in, codec), value);
            }
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @ParameterizedTest
    @MethodSource("testCasesForFile")
    protected <T> void encodeAndDecodeFile(FileIOTestCase<T> testCase, @TempDir Path dir) {
        FileIO io = testCase.io();
        T value = testCase.value();
        Codec<T> codec = testCase.codec();

        for (Path parent : this.createDirectoryPaths(dir)) { // success
            Path file = parent.resolve("success.tmp");
            ResultAssertions.assertSuccess(io.encodeTo(file, codec, value));
            ResultAssertions.assertSuccess(io.decodeFrom(file, codec), value);
        }

        for (Path parent : this.createDirectoryPaths(dir)) { // encode failure
            Path file = parent.resolve("encode_failure.tmp");
            EncodeError encodeError = EncodeError.failure("error");
            this.assertEncodeFailure(
                encodeError,
                ResultAssertions.assertFailure(io.encodeTo(file, ErrorCodec.encoder(encodeError), null))
            );
            Assertions.assertTrue(Files.exists(file));
        }

        for (Path parent : this.createDirectoryPaths(dir)) { // decode failure
            Path file = parent.resolve("decode_failure.tmp");
            ResultAssertions.assertSuccess(io.encodeTo(file, codec, value));

            DecodeError decodeError = DecodeError.failure("error");
            ResultAssertions.assertFailure(
                io.decodeFrom(file, ErrorCodec.decoder(decodeError)),
                decodeError
            );
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    protected <T> void encodeAndDecodeBytes(FileIOTestCase<T> testCase) {
        FileIO io = testCase.io();
        T value = testCase.value();
        Codec<T> codec = testCase.codec();

        { // success
            byte[] encoded = ResultAssertions.assertSuccess(io.encodeToBytes(codec, value));
            T decoded = ResultAssertions.assertSuccess(io.decodeBytes(codec, encoded));
            Assertions.assertEquals(value, decoded);
        }

        { // encode failure
            EncodeError encodeError = EncodeError.failure("error");
            this.assertEncodeFailure(
                encodeError,
                ResultAssertions.assertFailure(io.encodeToBytes(ErrorCodec.encoder(encodeError), null))
            );
        }

        { // decode failure
            byte[] encoded = ResultAssertions.assertSuccess(io.encodeToBytes(codec, value));

            DecodeError decodeError = DecodeError.failure("error");
            ResultAssertions.assertFailure(
                io.decodeBytes(ErrorCodec.decoder(decodeError), encoded),
                decodeError
            );
        }
    }

    @ParameterizedTest
    @MethodSource("implementations")
    protected void createParentDirectory(FileIO io, @TempDir Path dir) {
        Assertions.assertTrue(Files.isDirectory(dir));

        for (Path parent : this.createDirectoryPaths(dir)) {
            Path file = parent.resolve("test.txt");
            Assertions.assertDoesNotThrow(() -> io.createParentDirectory(file));
            Assertions.assertTrue(Files.isDirectory(parent));
        }
    }

    protected abstract Stream<FileIO> implementations();

    protected abstract Stream<FileIOTestCase<?>> createTestCases(Stream<FileIO> impls);

    protected Stream<FileIOTestCase<?>> testCases() {
        return this.createTestCases(this.implementations());
    }

    protected Stream<FileIOTestCase<?>> createTestCasesForFile(Stream<FileIO> impls) {
        return impls.flatMap(impl -> FileIOTestCase.fromSource(impl, MapSource.fromSourceWithStringKey(ValueSource.STRING)));
    }

    protected Stream<FileIOTestCase<?>> testCasesForFile() {
        return this.createTestCasesForFile(this.implementations());
    }

    protected abstract void assertEncodeFailure(EncodeError expected, EncodeError error);

    protected List<Path> createDirectoryPaths(Path dir) {
        return List.of(dir, dir.resolve("dir"), dir.resolve("dir1", "dir2"));
    }
}
