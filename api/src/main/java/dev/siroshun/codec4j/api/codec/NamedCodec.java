package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

record NamedCodec<T>(@NotNull String name, @NotNull Codec<T> codec) implements Codec<T> {

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return this.codec.decode(in);
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.codec.encode(out, input);
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public @NotNull Codec<T> named(@NotNull String name) {
        return new NamedCodec<>(name, this.codec);
    }
}
