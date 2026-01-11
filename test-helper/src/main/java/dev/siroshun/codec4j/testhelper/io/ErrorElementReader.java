package dev.siroshun.codec4j.testhelper.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

public record ErrorElementReader<I extends In>(boolean hasNext, DecodeError error) implements ElementReader<I> {

    public static <I extends In> @NotNull ElementReader<I> create(boolean hasNext, DecodeError error) {
        return new ErrorElementReader<>(hasNext, error);
    }

    @Override
    public @NotNull Result<I, DecodeError> next() {
        return this.error.asFailure();
    }

    @Override
    public @NotNull Result<Void, DecodeError> finish() {
        return this.error.asFailure();
    }

}
