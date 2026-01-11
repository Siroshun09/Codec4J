package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.tuple.TupleDecoder;
import dev.siroshun.codec4j.api.encoder.tuple.TupleEncoder;
import dev.siroshun.jfun.function.Function10;
import dev.siroshun.jfun.function.Function3;
import dev.siroshun.jfun.function.Function4;
import dev.siroshun.jfun.function.Function5;
import dev.siroshun.jfun.function.Function6;
import dev.siroshun.jfun.function.Function7;
import dev.siroshun.jfun.function.Function8;
import dev.siroshun.jfun.function.Function9;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class TupleCodec {

    @Contract("_, _ -> new")
    public static <T, V1> @NotNull Codec<T> create(@NotNull Function<V1, T> constructor, @NotNull TupleValueCodec<T, V1> codec1) {
        return Codec.codec(TupleEncoder.create(codec1), TupleDecoder.create(constructor, codec1));
    }

    @Contract("_, _, _ -> new")
    public static <T, V1, V2> @NotNull Codec<T> create(@NotNull BiFunction<V1, V2, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2) {
        return Codec.codec(TupleEncoder.create(codec1, codec2), TupleDecoder.create(constructor, codec1, codec2));
    }

    @Contract("_, _, _, _ -> new")
    public static <T, V1, V2, V3> @NotNull Codec<T> create(@NotNull Function3<V1, V2, V3, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3), TupleDecoder.create(constructor, codec1, codec2, codec3));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4> @NotNull Codec<T> create(@NotNull Function4<V1, V2, V3, V4, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5> @NotNull Codec<T> create(@NotNull Function5<V1, V2, V3, V4, V5, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6> @NotNull Codec<T> create(@NotNull Function6<V1, V2, V3, V4, V5, V6, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5, @NotNull TupleValueCodec<T, V6> codec6) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5, codec6), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5, codec6));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7> @NotNull Codec<T> create(@NotNull Function7<V1, V2, V3, V4, V5, V6, V7, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5, @NotNull TupleValueCodec<T, V6> codec6, @NotNull TupleValueCodec<T, V7> codec7) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5, codec6, codec7), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5, codec6, codec7));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8> @NotNull Codec<T> create(@NotNull Function8<V1, V2, V3, V4, V5, V6, V7, V8, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5, @NotNull TupleValueCodec<T, V6> codec6, @NotNull TupleValueCodec<T, V7> codec7, @NotNull TupleValueCodec<T, V8> codec8) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9> @NotNull Codec<T> create(@NotNull Function9<V1, V2, V3, V4, V5, V6, V7, V8, V9, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5, @NotNull TupleValueCodec<T, V6> codec6, @NotNull TupleValueCodec<T, V7> codec7, @NotNull TupleValueCodec<T, V8> codec8, @NotNull TupleValueCodec<T, V9> codec9) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8, codec9), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8, codec9));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> @NotNull Codec<T> create(@NotNull Function10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, T> constructor, @NotNull TupleValueCodec<T, V1> codec1, @NotNull TupleValueCodec<T, V2> codec2, @NotNull TupleValueCodec<T, V3> codec3, @NotNull TupleValueCodec<T, V4> codec4, @NotNull TupleValueCodec<T, V5> codec5, @NotNull TupleValueCodec<T, V6> codec6, @NotNull TupleValueCodec<T, V7> codec7, @NotNull TupleValueCodec<T, V8> codec8, @NotNull TupleValueCodec<T, V9> codec9, @NotNull TupleValueCodec<T, V10> codec10) {
        return Codec.codec(TupleEncoder.create(codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8, codec9, codec10), TupleDecoder.create(constructor, codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8, codec9, codec10));
    }

    private TupleCodec() {
        throw new UnsupportedOperationException();
    }
}
