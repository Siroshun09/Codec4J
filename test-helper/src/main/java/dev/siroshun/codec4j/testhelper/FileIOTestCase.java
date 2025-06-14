package dev.siroshun.codec4j.testhelper;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.testhelper.source.Source;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;

import java.util.stream.Stream;

public record FileIOTestCase<T>(FileIO io, Codec<T> codec, T value) {

    public static <T> Stream<FileIOTestCase<T>> fromSource(FileIO io, Source<T> source) {
        return source.values().map(value -> new FileIOTestCase<>(io, source.codec(), value));
    }

    public static Stream<FileIOTestCase<?>> forAllSources(FileIO io) {
        return Source.allSources().flatMap(source -> fromSource(io, source));
    }

    public void doTest() {
        byte[] encoded = ResultAssertions.assertSuccess(this.io.encodeToBytes(this.codec, this.value));
        T decoded = ResultAssertions.assertSuccess(this.io.decodeBytes(this.codec, encoded));
        Assertions.assertEquals(this.value, decoded);
    }

}
