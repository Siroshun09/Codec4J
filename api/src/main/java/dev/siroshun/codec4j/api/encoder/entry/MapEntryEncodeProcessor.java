package dev.siroshun.codec4j.api.encoder.entry;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

record MapEntryEncodeProcessor<K, V>(@NotNull Encoder<K> keyEncoder,
                                     @NotNull Encoder<V> valueEncoder) implements EntryEncoder.EncodeProcessor<Map.Entry<K, V>, Map<K, V>> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeKey(@NotNull Out<O> out, Map.@UnknownNullability Entry<K, V> entry) {
        return this.keyEncoder.encode(out, entry.getKey());
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeValue(@NotNull Out<O> out, Map.@UnknownNullability Entry<K, V> entry) {
        return this.valueEncoder.encode(out, entry.getValue());
    }

    @Override
    public @NotNull Iterator<Map.Entry<K, V>> toEntryIterator(@UnknownNullability Map<K, V> input) {
        return input == null ? Collections.emptyIterator() : input.entrySet().iterator();
    }
}
