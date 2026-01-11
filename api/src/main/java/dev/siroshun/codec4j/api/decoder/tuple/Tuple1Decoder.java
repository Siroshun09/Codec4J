package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

record Tuple1Decoder<T, V1>(Function<V1, T> constructor, TupleValueDecoder<V1> decoder1) implements Decoder<T> {

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        Result<ElementReader<? extends In>, DecodeError> readerResult = in.readList();
        if (readerResult.isFailure()) {
            return readerResult.asFailure();
        }

        ElementReader<? extends In> reader = readerResult.unwrap();

        Result<V1, DecodeError> v1Result = this.decoder1.decodeFromElementReader(reader, 0);
        if (v1Result.isFailure()) {
            return v1Result.asFailure();
        }

        return Result.success(this.constructor.apply(v1Result.unwrap()));
    }
}
