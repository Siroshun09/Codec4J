package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class EntryProcessors {

    private EntryProcessors() {
        throw new UnsupportedOperationException();
    }

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

    record MapEntryDecodeProcessor<K, V>(@NotNull Decoder<K> keyDecoder,
                                         @NotNull Decoder<V> valueDecoder,
                                         @NotNull Supplier<Map<K, V>> factory,
                                         boolean allowDuplicatedKey,
                                         @NotNull UnaryOperator<Map<K, V>> finalizer) implements EntryDecoder.DecodeProcessor<K, V, Map<K, V>> {

        @Override
        public @NotNull Result<K, DecodeError> decodeKey(@NotNull In in) {
            return this.keyDecoder.decode(in);
        }

        @Override
        public @NotNull Result<V, DecodeError> decodeValue(@NotNull In in) {
            return this.valueDecoder.decode(in);
        }

        @Override
        public @NotNull Map<K, V> createIdentity() {
            return this.factory.get();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptEntry(@NotNull Map<K, V> identity, @UnknownNullability K key, @UnknownNullability V value) {
            if (identity.putIfAbsent(key, value) != null && !this.allowDuplicatedKey) {
                return new DuplicatedKeyError(key, value).asFailure();
            }
            return Result.success();
        }

        @Override
        public @NotNull Map<K, V> finalizeIdentity(@NotNull Map<K, V> identity) {
            return this.finalizer.apply(identity);
        }
    }
}
