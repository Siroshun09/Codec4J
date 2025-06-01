package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Supplier;

public interface FieldDecoder<F> {

    static <F> @NotNull FieldDecoder<F> create(@NotNull String fieldName, @NotNull Decoder<F> decoder, @NotNull Supplier<Result<F, DecodeError>> onNotDecoded) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(decoder);
        Objects.requireNonNull(onNotDecoded);
        return new FieldDecoderImpl<>(fieldName, decoder, onNotDecoded);
    }

    static <F> @NotNull FieldDecoder<F> required(@NotNull String fieldName, @NotNull Decoder<F> decoder) {
        return create(fieldName, decoder, () -> new RequiredFieldError(fieldName).asFailure());
    }

    static <F> @NotNull FieldDecoder<F> optional(@NotNull String fieldName, @NotNull Decoder<F> decoder, @UnknownNullability F defaultValue) {
        return create(fieldName, decoder, () -> Result.success(defaultValue));
    }

    static <F> @NotNull FieldDecoder<F> supplying(@NotNull String fieldName, @NotNull Decoder<F> decoder, @NotNull Supplier<F> defaultValueSupplier) {
        Objects.requireNonNull(defaultValueSupplier);
        return create(fieldName, decoder, () -> Result.success(defaultValueSupplier.get()));
    }

    @NotNull String fieldName();

    @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in);

    @NotNull Result<F, DecodeError> onNotDecoded();

}
