package dev.siroshun.codec4j.api.encoder.object;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;
import java.util.function.Predicate;

record FieldEncoderImpl<T, F>(@NotNull String fieldName,
                              @NotNull Encoder<F> fieldEncoder,
                              @NotNull Function<T, F> getter,
                              @Nullable Predicate<T> canOmit) implements FieldEncoder<T> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.fieldEncoder.encode(out, this.getter.apply(input));
    }

    @Override
    public boolean canOmit(@UnknownNullability T input) {
        return this.canOmit != null && this.canOmit.test(input);
    }

}
