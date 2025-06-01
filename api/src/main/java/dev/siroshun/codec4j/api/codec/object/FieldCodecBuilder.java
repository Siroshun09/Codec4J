package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.object.FieldDecoder;
import dev.siroshun.codec4j.api.encoder.object.FieldEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class FieldCodecBuilder<F> {

    private final String fieldName;
    private final Codec<F> codec;
    private Supplier<Result<F, DecodeError>> defaultValueSupplier;

    FieldCodecBuilder(@NotNull String fieldName, @NotNull Codec<F> codec) {
        this.fieldName = fieldName;
        this.codec = codec;
    }

    public FieldCodecBuilder<F> defaultValue(F defaultValue) {
        this.defaultValueSupplier = () -> Result.success(defaultValue);
        return this;
    }

    public FieldCodecBuilder<F> defaultValueSupplier(Supplier<F> defaultValueSupplier) {
        this.defaultValueSupplier = () -> Result.success(defaultValueSupplier.get());
        return this;
    }

    public <T> FieldCodec<T, F> build(@NotNull Function<T, F> getter) {
        Objects.requireNonNull(getter);
        return FieldCodec.create(
            FieldEncoder.create(this.fieldName, this.codec, getter, null),
            this.defaultValueSupplier == null ?
                FieldDecoder.required(this.fieldName, this.codec) :
                FieldDecoder.create(this.fieldName, this.codec, this.defaultValueSupplier)
        );
    }

    public <T> FieldCodec<T, F> build(@NotNull Function<T, F> getter, @NotNull Predicate<T> omit) {
        Objects.requireNonNull(getter);
        Objects.requireNonNull(omit);
        return FieldCodec.create(
            FieldEncoder.create(this.fieldName, this.codec, getter, omit),
            this.defaultValueSupplier == null ?
                FieldDecoder.required(this.fieldName, this.codec) :
                FieldDecoder.create(this.fieldName, this.codec, this.defaultValueSupplier)
        );
    }
}
