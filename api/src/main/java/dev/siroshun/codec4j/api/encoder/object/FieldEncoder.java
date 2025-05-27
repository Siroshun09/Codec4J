package dev.siroshun.codec4j.api.encoder.object;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FieldEncoder<T> {

    @Contract("_, _, _, _ -> new")
    static <T, F> @NotNull FieldEncoder<T> create(@NotNull String fieldName, @NotNull Encoder<F> fieldEncoder, @NotNull Function<T, F> getter, @Nullable Predicate<T> canOmit) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(fieldEncoder);
        Objects.requireNonNull(getter);
        return new FieldEncoderImpl<>(fieldName, fieldEncoder, getter, canOmit);
    }

    @NotNull String fieldName();

    <O> @NotNull Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input);

    boolean canOmit(@UnknownNullability T input);

}
