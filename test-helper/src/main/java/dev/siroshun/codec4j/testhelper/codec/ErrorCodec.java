package dev.siroshun.codec4j.testhelper.codec;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ErrorCodec<T>(EncodeError encodeError, DecodeError decodeError) implements Codec<T> {

    public static <T> @NotNull Codec<T> create(EncodeError encodeError, DecodeError decodeError) {
        return new ErrorCodec<>(Objects.requireNonNull(encodeError), Objects.requireNonNull(decodeError));
    }

    public static <T> @NotNull Encoder<T> encoder(EncodeError encodeError) {
        return new ErrorCodec<>(Objects.requireNonNull(encodeError), null);
    }

    public static <T> @NotNull Codec<T> decoder(DecodeError decodeError) {
        return new ErrorCodec<>(null, Objects.requireNonNull(decodeError));
    }

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return Result.failure(this.decodeError());
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, T input) {
        return Result.failure(this.encodeError());
    }
}
