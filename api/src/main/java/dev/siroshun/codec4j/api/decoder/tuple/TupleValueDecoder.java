package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface TupleValueDecoder<V> {

    static <V> @NotNull TupleValueDecoder<V> create(@NotNull Decoder<V> decoder) {
        return new TupleValueDecoderImpl<>(Objects.requireNonNull(decoder));
    }

    @NotNull Result<V, DecodeError> decodeFromElementReader(@NotNull ElementReader<?> reader, int index);

}
