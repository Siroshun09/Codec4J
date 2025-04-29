package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;
import java.util.function.Supplier;

public interface FieldCodec<T, F> {

    @NotNull String fieldName();

    @NotNull Codec<F> codec();

    @UnknownNullability
    F getFieldValue(@UnknownNullability T object);

    @NotNull Result<F, DecodeError> fallbackValue();

    default @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in) {
        return this.codec().decode(in);
    }

    default <O> @NotNull Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.codec().encode(out, this.getFieldValue(input));
    }

    record Required<T, F>(String fieldName, Codec<F> codec, Function<T, F> getter) implements FieldCodec<T, F> {

        @Override
        public @UnknownNullability F getFieldValue(@UnknownNullability T object) {
            return this.getter.apply(object);
        }

        @Override
        public @NotNull Result<F, DecodeError> fallbackValue() {
            return new RequiredFieldError(this.fieldName).asFailure();
        }

    }

    record DefaultValue<T, F>(String fieldName, Codec<F> codec, Function<T, F> getter,
                              @Nullable F defaultValue) implements FieldCodec<T, F> {

        @Override
        public @UnknownNullability F getFieldValue(@UnknownNullability T object) {
            return this.getter.apply(object);
        }

        @Override
        public Result.@NotNull Success<F, DecodeError> fallbackValue() {
            return Result.success(this.defaultValue);
        }

    }

    record DefaultValueSupplier<T, F>(String fieldName, Codec<F> codec, Function<T, F> getter,
                                      Supplier<? extends F> supplier) implements FieldCodec<T, F> {

        @Override
        public @UnknownNullability F getFieldValue(@UnknownNullability T object) {
            return this.getter.apply(object);
        }

        @Override
        public Result.@NotNull Success<F, DecodeError> fallbackValue() {
            return Result.success(this.supplier.get());
        }

    }

    record RequiredFieldError(String fieldName) implements DecodeError.Failure {
    }

    record AlreadyDecodedError(String fieldName) implements DecodeError.Failure {
    }
}
