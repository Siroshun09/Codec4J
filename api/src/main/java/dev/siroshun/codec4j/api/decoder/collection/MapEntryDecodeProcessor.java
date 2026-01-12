package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
