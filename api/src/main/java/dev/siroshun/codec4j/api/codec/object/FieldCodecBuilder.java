package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class FieldCodecBuilder<T, F> {

    private final String fieldName;
    private final Codec<F> codec;
    private final Function<T, F> getter;

    FieldCodecBuilder(@NotNull String fieldName, @NotNull Codec<F> codec, @NotNull Function<T, F> getter) {
        this.fieldName = fieldName;
        this.codec = codec;
        this.getter = getter;
    }

    @Contract(" -> new")
    public @NotNull FieldCodec<T, F> required() {
        return this.build(null, () -> new FieldCodec.RequiredFieldError(this.fieldName).asFailure());
    }

    @Contract(" -> new")
    public @NotNull FieldCodec<T, F> optional() {
        return this.build(null, Result::success);
    }

    @Contract("_ -> new")
    public @NotNull FieldCodec<T, F> optional(@NotNull Predicate<T> omit) {
        Objects.requireNonNull(omit);
        return this.build(omit, Result::success);
    }

    @Contract("_ -> new")
    public @NotNull FieldCodec<T, F> withDefaultValue(@Nullable F defaultValue) {
        return this.build(null, () -> Result.success(defaultValue));
    }

    @Contract("_, _ -> new")
    public @NotNull FieldCodec<T, F> withDefaultValue(@Nullable F defaultValue, @NotNull Predicate<T> omit) {
        Objects.requireNonNull(omit);
        return this.build(omit, () -> Result.success(defaultValue));
    }

    @Contract("_ -> new")
    public @NotNull FieldCodec<T, F> withDefaultValueSupplier(@NotNull Supplier<F> defaultValueSupplier) {
        Objects.requireNonNull(defaultValueSupplier);
        return this.build(null, () -> Result.success(defaultValueSupplier.get()));
    }

    @Contract("_, _ -> new")
    public @NotNull FieldCodec<T, F> withDefaultValueSupplier(@NotNull Supplier<F> defaultValueSupplier, @NotNull Predicate<T> omit) {
        Objects.requireNonNull(omit);
        return this.build(omit, () -> Result.success(defaultValueSupplier.get()));
    }

    private @NotNull FieldCodec<T, F> build(@Nullable Predicate<T> omit, @NotNull Supplier<Result<F, DecodeError>> onNotDecoded) {
        return new FieldCodecImpl<>(this.fieldName, this.codec, this.getter, omit, onNotDecoded);
    }

    private record FieldCodecImpl<T, F>(@NotNull String fieldName,
                                        @NotNull Codec<F> codec, @NotNull Function<T, F> getter,
                                        @Nullable Predicate<T> canOmit,
                                        @NotNull Supplier<Result<F, DecodeError>> onNotDecodedSupplier) implements FieldCodec<T, F> {

        @Override
        public @NotNull <O> Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input) {
            return this.codec.encode(out, this.getter.apply(input));
        }

        @Override
        public boolean canOmit(@UnknownNullability T input) {
            return this.canOmit != null && this.canOmit.test(input);
        }

        @Override
        public @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in) {
            return this.codec.decode(in);
        }

        @Override
        public @NotNull Result<F, DecodeError> onNotDecoded() {
            return this.onNotDecodedSupplier.get();
        }
    }
}
