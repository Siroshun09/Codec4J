package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

record TupleValueDecoderImpl<V>(Decoder<V> decoder) implements TupleValueDecoder<V> {

    @Override
    public @NotNull Result<V, DecodeError> decodeFromElementReader(@NotNull ElementReader<?> reader, int index)  {
        if (!reader.hasNext()) {
            return DecodeError.noElementError(index).asFailure();
        }

        Result<? extends In, DecodeError> in = reader.next();
        if (in.isFailure()) {
            return in.asFailure();
        }

        return this.decoder().decode(in.unwrap());
    }

}
