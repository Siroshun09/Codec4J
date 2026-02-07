package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.function.Function7;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

record Tuple7Decoder<T, V1, V2, V3, V4, V5, V6, V7>(Function7<V1, V2, V3, V4, V5, V6, V7, T> constructor,
                                                    TupleValueDecoder<V1> decoder1,
                                                    TupleValueDecoder<V2> decoder2,
                                                    TupleValueDecoder<V3> decoder3,
                                                    TupleValueDecoder<V4> decoder4,
                                                    TupleValueDecoder<V5> decoder5,
                                                    TupleValueDecoder<V6> decoder6,
                                                    TupleValueDecoder<V7> decoder7) implements Decoder<T> {

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

        Result<V3, DecodeError> v3Result = this.decoder3.decodeFromElementReader(reader, 2);
        if (v3Result.isFailure()) {
            return v3Result.asFailure();
        }

        Result<V4, DecodeError> v4Result = this.decoder4.decodeFromElementReader(reader, 3);
        if (v4Result.isFailure()) {
            return v4Result.asFailure();
        }

        Result<V5, DecodeError> v5Result = this.decoder5.decodeFromElementReader(reader, 4);
        if (v5Result.isFailure()) {
            return v5Result.asFailure();
        }

        Result<V6, DecodeError> v6Result = this.decoder6.decodeFromElementReader(reader, 5);
        if (v6Result.isFailure()) {
            return v6Result.asFailure();
        }

        Result<V7, DecodeError> v7Result = this.decoder7.decodeFromElementReader(reader, 6);
        if (v7Result.isFailure()) {
            return v7Result.asFailure();
        }

        Result<Void, DecodeError> finishResult = reader.finish();
        if (finishResult.isFailure()) {
            return finishResult.asFailure();
        }

        return Result.success(this.constructor.apply(v1Result.unwrap(), v2Result.unwrap(), v3Result.unwrap(), v4Result.unwrap(), v5Result.unwrap(), v6Result.unwrap(), v7Result.unwrap()));
    }
}
