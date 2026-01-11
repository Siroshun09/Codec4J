package dev.siroshun.codec4j.testhelper.io;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ErrorOut<O>(@NotNull EncodeError error) implements Out<O> {

    public static <O> @NotNull ErrorOut<O> create(@NotNull EncodeError error) {
        return new ErrorOut<>(Objects.requireNonNull(error));
    }

    @Override
    public @NotNull Result<O, EncodeError> writeBoolean(boolean value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeByte(byte value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeChar(char value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeDouble(double value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeFloat(float value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeInt(int value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeLong(long value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeShort(short value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<O, EncodeError> writeString(@NotNull String value) {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<ElementAppender<O>, EncodeError> createList() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<EntryAppender<O>, EncodeError> createMap() {
        return this.error.asFailure();
    }

}
