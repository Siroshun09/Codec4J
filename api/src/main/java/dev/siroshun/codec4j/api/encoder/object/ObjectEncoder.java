package dev.siroshun.codec4j.api.encoder.object;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collection;
import java.util.List;

public final class ObjectEncoder<T> implements Encoder<T> {

    @Contract("_ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1) {
        return new ObjectEncoder<>(List.of(field1));
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2) {
        return new ObjectEncoder<>(List.of(field1, field2));
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3) {
        return new ObjectEncoder<>(List.of(field1, field2, field3));
    }

    @Contract("_, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5, @NotNull FieldEncoder<T> field6) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5, field6));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5, @NotNull FieldEncoder<T> field6, @NotNull FieldEncoder<T> field7) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5, field6, field7));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5, @NotNull FieldEncoder<T> field6, @NotNull FieldEncoder<T> field7, @NotNull FieldEncoder<T> field8) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5, @NotNull FieldEncoder<T> field6, @NotNull FieldEncoder<T> field7, @NotNull FieldEncoder<T> field8, @NotNull FieldEncoder<T> field9) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8, field9));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull FieldEncoder<T> field1, @NotNull FieldEncoder<T> field2, @NotNull FieldEncoder<T> field3, @NotNull FieldEncoder<T> field4, @NotNull FieldEncoder<T> field5, @NotNull FieldEncoder<T> field6, @NotNull FieldEncoder<T> field7, @NotNull FieldEncoder<T> field8, @NotNull FieldEncoder<T> field9, @NotNull FieldEncoder<T> field10) {
        return new ObjectEncoder<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10));
    }

    @Contract("_ -> new")
    public static <T> @NotNull ObjectEncoder<T> create(@NotNull Collection<FieldEncoder<T>> fields) {
        return new ObjectEncoder<>(List.copyOf(fields));
    }

    private final List<FieldEncoder<T>> fields;

    private ObjectEncoder(List<FieldEncoder<T>> fields) {
        this.fields = fields;
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Result<EntryAppender<O>, EncodeError> result = out.createMap();

        if (result.isFailure()) {
            return result.asFailure();
        }

        EntryAppender<O> appender = result.unwrap();

        for (var field : this.fields) {
            if (field.canOmit(input)) {
                continue;
            }

            Result<Void, EncodeError> encodeResult = appender.append(
                keyOut -> keyOut.writeString(field.fieldName()),
                valueOut -> field.encodeFieldValue(valueOut, input)
            );
            if (encodeResult.isFailure()) {
                return result.asFailure();
            }
        }

        return appender.finish();
    }
}
