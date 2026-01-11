package dev.siroshun.codec4j.api.encoder.tuple;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;

record TupleValueEncoderImpl<T, V>(@NotNull Encoder<V> encoder,
                                   @NotNull Function<T, V> getter) implements TupleValueEncoder<T> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeValue(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.encoder.encode(out, this.getter.apply(input));
    }

}
