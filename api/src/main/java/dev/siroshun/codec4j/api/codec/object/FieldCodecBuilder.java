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

    public <T> @NotNull FieldCodec<T, F> required(@NotNull Function<T, F> getter) {
        return this.build(getter, null, Objects.requireNonNullElseGet(
            this.defaultValueSupplier,
            () -> () -> new FieldCodec.RequiredFieldError(this.fieldName).asFailure()
        ));
    }

    @Contract("_ -> new")
    public <T> @NotNull FieldCodec<T, F> optional(@NotNull Function<T, F> getter) {
        return this.build(getter, null, Objects.requireNonNullElseGet(this.defaultValueSupplier, () -> Result::success));
    }

    @Contract("_, _ -> new")
    public <T> @NotNull FieldCodec<T, F> optional(@NotNull Function<T, F> getter, @NotNull Predicate<T> omit) {
        return this.build(getter, omit, Objects.requireNonNullElseGet(this.defaultValueSupplier, () -> Result::success)
        );
    }

    private <T> @NotNull FieldCodec<T, F> build(@NotNull Function<T, F> getter, @Nullable Predicate<T> omit, @NotNull Supplier<Result<F, DecodeError>> onNotDecoded) {
        return new FieldCodecImpl<>(this.fieldName, this.codec, getter, omit, onNotDecoded);
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
