package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;

record SimpleCodec<T>(@NotNull Encoder<? super T> encoder, @NotNull Decoder<? extends T> decoder) implements Codec<T> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.encoder.encode(out, input);
    }

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return this.decoder.decode(in).map(Function.identity());
    }

    @Override
    public <A> @NotNull Codec<A> xmap(@NotNull Function<? super A, ? extends T> fromA, @NotNull Function<? super T, ? extends A> toA) {
        return new SimpleCodec<>(this.encoder.comap(fromA), this.decoder.map(toA));
    }

    @Override
    public <A> @NotNull Codec<A> flatXmap(@NotNull Function<? super A, Result<T, EncodeError>> fromA, @NotNull Function<? super T, Result<A, DecodeError>> toA) {
        return new SimpleCodec<>(this.encoder.flatComap(fromA), this.decoder.flatMap(toA));
    }

    @Override
    public @NotNull Codec<T> named(@NotNull String name) {
        Objects.requireNonNull(name);
        return new NamedCodec<>(name, this);
    }
}
