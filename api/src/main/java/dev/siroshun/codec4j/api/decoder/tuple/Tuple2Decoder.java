package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

record Tuple2Decoder<T, V1, V2>(BiFunction<V1, V2, T> constructor,
                                TupleValueDecoder<V1> decoder1,
                                TupleValueDecoder<V2> decoder2) implements Decoder<T> {

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

        Result<V2, DecodeError> v2Result = this.decoder2.decodeFromElementReader(reader, 1);
        if (v2Result.isFailure()) {
            return v2Result.asFailure();
        }

        return Result.success(this.constructor.apply(v1Result.unwrap(), v2Result.unwrap()));
    }
}
