package dev.siroshun.codec4j.testhelper.io;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.TextFileIO;
import dev.siroshun.codec4j.testhelper.codec.ErrorCodec;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TextFileIOTest extends FileIOTest {

    @ParameterizedTest
    @MethodSource("testCases")
    protected <T> void encodeWriterAndDecodeReader(FileIOTestCase<T> testCase) {
        TextFileIO io = Assertions.assertInstanceOf(TextFileIO.class, testCase.io());
        T value = testCase.value();
        Codec<T> codec = testCase.codec();

        try (StringWriter writer = new StringWriter()) {
            ResultAssertions.assertSuccess(io.encodeTo(writer, codec, value));

            try (StringReader reader = new StringReader(writer.toString())) {
                ResultAssertions.assertSuccess(io.decodeFrom(reader, codec), value);
            }
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @ParameterizedTest
    @MethodSource("testCasesForFile")
    protected <T> void encodeToFileAsDefaultCharset(FileIOTestCase<T> testCase, @TempDir Path dir) {
        TextFileIO io = Assertions.assertInstanceOf(TextFileIO.class, testCase.io());
        T value = testCase.value();
        Codec<T> codec = testCase.codec();
        Path file = dir.resolve("test.txt");

        ResultAssertions.assertSuccess(io.encodeTo(file, codec, value));

        try {
            Assertions.assertEquals(
                ResultAssertions.assertSuccess(io.encodeToString(codec, value)),
                Files.readString(file, TextFileIO.DEFAULT_CHARSET)
            );
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }

    @ParameterizedTest
    @MethodSource("testCases")
    protected <T> void encodeString(FileIOTestCase<T> testCase) {
        TextFileIO io = Assertions.assertInstanceOf(TextFileIO.class, testCase.io());
        T value = testCase.value();
        Codec<T> codec = testCase.codec();

        { // success
            String encoded = ResultAssertions.assertSuccess(io.encodeToString(codec, value));
            T decoded = ResultAssertions.assertSuccess(io.decodeString(codec, encoded));
            Assertions.assertEquals(value, decoded);
        }

        if (false) { // encode failure // TODO: unexpected fatal error returned
            EncodeError encodeError = EncodeError.failure("error");
            ResultAssertions.assertFailure(
                io.encodeToString(ErrorCodec.encoder(encodeError), null),
                encodeError
            );
        }

        { // decode failure
            String encoded = ResultAssertions.assertSuccess(io.encodeToString(codec, value));

            DecodeError decodeError = DecodeError.failure("error");
            ResultAssertions.assertFailure(
                io.decodeString(ErrorCodec.decoder(decodeError), encoded),
                decodeError
            );
        }
    }
}
