package dev.siroshun.codec4j.testhelper.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.EntryReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.function.BiFunction;

public record ErrorIn(DecodeError error) implements In {

    public static @NotNull In create(@NotNull DecodeError error) {
        return new ErrorIn(Objects.requireNonNull(error));
    }

    @Override
    public @NotNull Result<Type, DecodeError> type() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Boolean, DecodeError> readAsBoolean() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Byte, DecodeError> readAsByte() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Character, DecodeError> readAsChar() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Double, DecodeError> readAsDouble() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Float, DecodeError> readAsFloat() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Integer, DecodeError> readAsInt() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Long, DecodeError> readAsLong() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Short, DecodeError> readAsShort() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<String, DecodeError> readAsString() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<ElementReader<? extends In>, DecodeError> readList() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull <R> Result<R, DecodeError> readList(@NonNull R identity, @NotNull BiFunction<R, ? super In, Result<?, ?>> operator) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<EntryReader, DecodeError> readMap() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull <R> Result<R, DecodeError> readMap(@NonNull R identity, @NotNull BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Void, DecodeError> skip() {
        return this.error.asFailure();
    }
}
