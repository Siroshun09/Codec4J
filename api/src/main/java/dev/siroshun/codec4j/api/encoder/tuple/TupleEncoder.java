package dev.siroshun.codec4j.api.encoder.tuple;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public final class TupleEncoder<T> implements Encoder<T> {

    @Contract("_ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1) {
        return new TupleEncoder<>(List.of(value1));
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2) {
        return new TupleEncoder<>(List.of(value1, value2));
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3) {
        return new TupleEncoder<>(List.of(value1, value2, value3));
    }

    @Contract("_, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5, @NotNull TupleValueEncoder<T> value6) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5, value6));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5, @NotNull TupleValueEncoder<T> value6, @NotNull TupleValueEncoder<T> value7) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5, value6, value7));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5, @NotNull TupleValueEncoder<T> value6, @NotNull TupleValueEncoder<T> value7, @NotNull TupleValueEncoder<T> value8) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5, value6, value7, value8));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5, @NotNull TupleValueEncoder<T> value6, @NotNull TupleValueEncoder<T> value7, @NotNull TupleValueEncoder<T> value8, @NotNull TupleValueEncoder<T> value9) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5, value6, value7, value8, value9));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull TupleValueEncoder<T> value1, @NotNull TupleValueEncoder<T> value2, @NotNull TupleValueEncoder<T> value3, @NotNull TupleValueEncoder<T> value4, @NotNull TupleValueEncoder<T> value5, @NotNull TupleValueEncoder<T> value6, @NotNull TupleValueEncoder<T> value7, @NotNull TupleValueEncoder<T> value8, @NotNull TupleValueEncoder<T> value9, @NotNull TupleValueEncoder<T> value10) {
        return new TupleEncoder<>(List.of(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10));
    }

    @Contract("_ -> new")
    public static <T> @NotNull Encoder<T> create(@NotNull Collection<TupleValueEncoder<T>> values) {
        return new TupleEncoder<>(List.copyOf(values));
    }

    private final List<TupleValueEncoder<T>> values;

    private TupleEncoder(List<TupleValueEncoder<T>> values) {
        this.values = values;
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, T input) {
        Result<ElementAppender<O>, EncodeError> appenderResult = out.createList();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        ElementAppender<O> appender = appenderResult.unwrap();
        for (TupleValueEncoder<T> encoder : this.values) {
            Result<O, EncodeError> elementResult = appender.append(elementOut -> encoder.encodeValue(elementOut, input));
            if (elementResult.isFailure()) {
                return elementResult.asFailure();
            }
        }

        return appender.finish();
    }

}
