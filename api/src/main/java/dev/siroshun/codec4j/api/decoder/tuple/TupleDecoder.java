package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
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

public final class TupleDecoder {

    @Contract("_, _ -> new")
    public static <T, V1> @NotNull Decoder<T> create(@NotNull Function<V1, T> constructor, @NotNull TupleValueDecoder<V1> decoder1) {
        return new Tuple1Decoder<>(constructor, decoder1);
    }

    @Contract("_, _, _ -> new")
    public static <T, V1, V2> @NotNull Decoder<T> create(@NotNull BiFunction<V1, V2, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2) {
        return new Tuple2Decoder<>(constructor, decoder1, decoder2);
    }

    @Contract("_, _, _, _ -> new")
    public static <T, V1, V2, V3> @NotNull Decoder<T> create(@NotNull Function3<V1, V2, V3, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3) {
        return new Tuple3Decoder<>(constructor, decoder1, decoder2, decoder3);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4> @NotNull Decoder<T> create(@NotNull Function4<V1, V2, V3, V4, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4) {
        return new Tuple4Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4);
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5> @NotNull Decoder<T> create(@NotNull Function5<V1, V2, V3, V4, V5, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5) {
        return new Tuple5Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5);
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6> @NotNull Decoder<T> create(@NotNull Function6<V1, V2, V3, V4, V5, V6, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5, @NotNull TupleValueDecoder<V6> decoder6) {
        return new Tuple6Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5, decoder6);
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7> @NotNull Decoder<T> create(@NotNull Function7<V1, V2, V3, V4, V5, V6, V7, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5, @NotNull TupleValueDecoder<V6> decoder6, @NotNull TupleValueDecoder<V7> decoder7) {
        return new Tuple7Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5, decoder6, decoder7);
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8> @NotNull Decoder<T> create(@NotNull Function8<V1, V2, V3, V4, V5, V6, V7, V8, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5, @NotNull TupleValueDecoder<V6> decoder6, @NotNull TupleValueDecoder<V7> decoder7, @NotNull TupleValueDecoder<V8> decoder8) {
        return new Tuple8Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5, decoder6, decoder7, decoder8);
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9> @NotNull Decoder<T> create(@NotNull Function9<V1, V2, V3, V4, V5, V6, V7, V8, V9, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5, @NotNull TupleValueDecoder<V6> decoder6, @NotNull TupleValueDecoder<V7> decoder7, @NotNull TupleValueDecoder<V8> decoder8, @NotNull TupleValueDecoder<V9> decoder9) {
        return new Tuple9Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5, decoder6, decoder7, decoder8, decoder9);
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10> @NotNull Decoder<T> create(@NotNull Function10<V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, T> constructor, @NotNull TupleValueDecoder<V1> decoder1, @NotNull TupleValueDecoder<V2> decoder2, @NotNull TupleValueDecoder<V3> decoder3, @NotNull TupleValueDecoder<V4> decoder4, @NotNull TupleValueDecoder<V5> decoder5, @NotNull TupleValueDecoder<V6> decoder6, @NotNull TupleValueDecoder<V7> decoder7, @NotNull TupleValueDecoder<V8> decoder8, @NotNull TupleValueDecoder<V9> decoder9, @NotNull TupleValueDecoder<V10> decoder10) {
        return new Tuple10Decoder<>(constructor, decoder1, decoder2, decoder3, decoder4, decoder5, decoder6, decoder7, decoder8, decoder9, decoder10);
    }

    private TupleDecoder() {
        throw new UnsupportedOperationException();
    }
}
