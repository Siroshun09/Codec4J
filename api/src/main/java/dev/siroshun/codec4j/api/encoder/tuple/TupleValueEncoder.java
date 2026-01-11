package dev.siroshun.codec4j.api.encoder.tuple;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;

public interface TupleValueEncoder<T> {

    static <T, V> @NotNull TupleValueEncoder<T> create(@NotNull Encoder<V> encoder, @NotNull Function<T, V> getter) {
        Objects.requireNonNull(encoder);
        Objects.requireNonNull(getter);
        return new TupleValueEncoderImpl<>(encoder, getter);
    }

    <O> @NotNull Result<O, EncodeError> encodeValue(@NotNull Out<O> out, @UnknownNullability T input);

}
