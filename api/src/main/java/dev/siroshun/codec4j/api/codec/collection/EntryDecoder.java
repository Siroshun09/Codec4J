package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Decoder} for decoding entries from an {@link In#readMap(Object, java.util.function.BiFunction)}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @param <R> the type of the source collection to decode
 */
public interface EntryDecoder<K, V, R> extends Decoder<R> {

    /**
     * Returns the {@link EntryProcessor} for decoding entries and collect them.
     *
     * @return the {@link EntryProcessor}
     */
    @NotNull EntryProcessor<K, V, ?, R> processor();

    @Override
    default @NotNull Result<R, DecodeError> decode(@NotNull In in) {
        return in.readMap(this.processor().createIdentity(), (identity, entryIn) -> {
            Result<K, DecodeError> keyResult = this.processor().decodeKey(entryIn.keyIn());
            Result<V, DecodeError> valueResult = this.processor().decodeValue(entryIn.valueIn());

            if (keyResult.isFailure()) {
                return keyResult.unwrapError().isIgnorable() ? Result.success() : keyResult.asFailure();
            }

            if (valueResult.isFailure()) {
                return valueResult.unwrapError().isIgnorable() ? Result.success() : valueResult.asFailure();
            }

            K key = keyResult.unwrap();
            V value = valueResult.unwrap();
            Result<Void, DecodeError> acceptResult = this.processor().acceptEntry(identity, key, value);

            if (acceptResult.isFailure() && !acceptResult.unwrapError().isIgnorable()) {
                return acceptResult.asFailure();
            }

            return Result.success();
        }).map(this.processor()::finalizeIdentity);
    }
}
