package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.decoder.tuple.TupleValueDecoder;
import dev.siroshun.codec4j.api.encoder.tuple.TupleValueEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

record TupleValueCodecImpl<T, V>(TupleValueEncoder<T> valueEncoder,
                                 TupleValueDecoder<V> valueDecoder) implements TupleValueCodec<T, V> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeValue(@NotNull Out<O> out, T input) {
        return this.valueEncoder.encodeValue(out, input);
    }

    @Override
    public @NotNull Result<V, DecodeError> decodeFromElementReader(@NotNull ElementReader<?> reader, int index) {
        return this.valueDecoder.decodeFromElementReader(reader, index);
    }
}
