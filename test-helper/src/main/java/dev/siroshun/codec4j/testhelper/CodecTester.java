package dev.siroshun.codec4j.testhelper;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Consumer;

public final class CodecTester {

    public static <T> void test(@NotNull Codec<T> codec, @UnknownNullability T input) {
        Result<Memory, EncodeError> encodeResult = codec.encode(Memory.out(), input);
        ResultAssertions.assertSuccess(encodeResult);

        Result<T, DecodeError> decodeResult = codec.decode(encodeResult.unwrap());
        ResultAssertions.assertSuccess(decodeResult, input);
    }

    public static <T> void test(@NotNull Codec<T> codec, @UnknownNullability T input, @NotNull Consumer<T> decodedValueChecker) {
        Result<Memory, EncodeError> encodeResult = codec.encode(Memory.out(), input);
        ResultAssertions.assertSuccess(encodeResult);

        Result<T, DecodeError> decodeResult = codec.decode(encodeResult.unwrap());
        decodedValueChecker.accept(ResultAssertions.assertSuccess(decodeResult));
    }

    private CodecTester() {
        throw new UnsupportedOperationException();
    }
}
