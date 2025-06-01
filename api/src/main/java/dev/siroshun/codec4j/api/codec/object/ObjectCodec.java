package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.object.ObjectDecoder;
import dev.siroshun.codec4j.api.encoder.object.ObjectEncoder;
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

public final class ObjectCodec {

    @Contract("_, _ -> new")
    public static <T, F1> @NotNull Codec<T> create(@NotNull Function<F1, T> constructor, @NotNull FieldCodec<T, F1> field1) {
        return Codec.codec(ObjectEncoder.create(field1), ObjectDecoder.create(constructor, field1));
    }

    @Contract("_, _, _ -> new")
    public static <T, F1, F2> @NotNull Codec<T> create(@NotNull BiFunction<F1, F2, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2) {
        return Codec.codec(ObjectEncoder.create(field1, field2), ObjectDecoder.create(constructor, field1, field2));
    }

    @Contract("_, _, _, _ -> new")
    public static <T, F1, F2, F3> @NotNull Codec<T> create(@NotNull Function3<F1, F2, F3, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3), ObjectDecoder.create(constructor, field1, field2, field3));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4> @NotNull Codec<T> create(@NotNull Function4<F1, F2, F3, F4, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4), ObjectDecoder.create(constructor, field1, field2, field3, field4));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5> @NotNull Codec<T> create(@NotNull Function5<F1, F2, F3, F4, F5, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6> @NotNull Codec<T> create(@NotNull Function6<F1, F2, F3, F4, F5, F6, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5, field6), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5, field6));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7> @NotNull Codec<T> create(@NotNull Function7<F1, F2, F3, F4, F5, F6, F7, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5, field6, field7), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5, field6, field7));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8> @NotNull Codec<T> create(@NotNull Function8<F1, F2, F3, F4, F5, F6, F7, F8, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5, field6, field7, field8), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9> @NotNull Codec<T> create(@NotNull Function9<F1, F2, F3, F4, F5, F6, F7, F8, F9, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8, @NotNull FieldCodec<T, F9> field9) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5, field6, field7, field8, field9), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> @NotNull Codec<T> create(@NotNull Function10<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8, @NotNull FieldCodec<T, F9> field9, @NotNull FieldCodec<T, F10> field10) {
        return Codec.codec(ObjectEncoder.create(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10), ObjectDecoder.create(constructor, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    private ObjectCodec() {
        throw new UnsupportedOperationException();
    }
}
