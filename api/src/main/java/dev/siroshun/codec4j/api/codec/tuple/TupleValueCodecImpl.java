package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.decoder.tuple.TupleValueDecoder;
import dev.siroshun.codec4j.api.encoder.tuple.TupleValueEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

record TupleValueCodecImpl<T, V>(TupleValueEncoder<T> encoder, TupleValueDecoder<V> decoder,
                                 @Nullable String name) implements TupleValueCodec<T, V> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeValue(@NotNull Out<O> out, T input) {
        return this.encoder.encodeValue(out, input);
    }

    @Override
    public @NotNull Result<V, DecodeError> decodeFromElementReader(@NotNull ElementReader<?> reader, int index) {
        return this.decoder.decodeFromElementReader(reader, index);
    }

    @Override
    public @NotNull String toString() {
        if (this.name != null) {
            return this.name;
        }
        return "TupleValueCodec{" +
               "encoder=" + this.encoder +
               ", decoder=" + this.decoder +
               '}';
    }
}
