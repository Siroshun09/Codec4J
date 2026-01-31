package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.tuple.TupleValueDecoder;
import dev.siroshun.codec4j.api.encoder.tuple.TupleValueEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface TupleValueCodec<T, V> extends TupleValueEncoder<T>, TupleValueDecoder<V> {

    static <T, V> @NotNull TupleValueCodec<T, V> create(@NotNull TupleValueEncoder<T> encoder, @NotNull TupleValueDecoder<V> decoder) {
        Objects.requireNonNull(encoder);
        Objects.requireNonNull(decoder);
        return new TupleValueCodecImpl<>(encoder, decoder, null);
    }

    static <T, V> @NotNull TupleValueCodec<T, V> create(@NotNull TupleValueEncoder<T> encoder, @NotNull TupleValueDecoder<V> decoder, @NotNull String name) {
        Objects.requireNonNull(encoder);
        Objects.requireNonNull(decoder);
        Objects.requireNonNull(name);
        return new TupleValueCodecImpl<>(encoder, decoder, name);
    }

    static <T, V> @NotNull TupleValueCodec<T, V> create(@NotNull Codec<V> codec, @NotNull Function<T, V> getter) {
        return create(TupleValueEncoder.create(codec, getter), TupleValueDecoder.create(codec), codec.toString());
    }
}
