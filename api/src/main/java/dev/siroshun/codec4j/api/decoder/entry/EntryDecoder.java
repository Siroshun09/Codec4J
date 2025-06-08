package dev.siroshun.codec4j.api.decoder.entry;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A {@link Decoder} for decoding entries from an {@link In#readMap(Object, java.util.function.BiFunction)}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @param <R> the type of the source collection to decode
 */
public interface EntryDecoder<K, V, R> extends Decoder<R> {

    /**
     * Creates a {@link EntryDecoder} for decoding entries as a {@link Map}.
     * <p>
     * This decoder does not allow duplicate keys.
     *
     * @param keyDecoder   the {@link Decoder} for decoding keys
     * @param valueDecoder the {@link Decoder} for decoding values
     * @param <K>          the type of the key
     * @param <V>          the type of the value
     * @return a {@link EntryDecoder} for decoding entries as a {@link Map}
     */
    static <K, V> @NotNull Decoder<Map<K, V>> map(@NotNull Decoder<K> keyDecoder, @NotNull Decoder<V> valueDecoder) {
        Objects.requireNonNull(keyDecoder);
        Objects.requireNonNull(valueDecoder);
        return map(keyDecoder, valueDecoder, HashMap::new, false, UnaryOperator.identity());
    }

    /**
     * Creates a {@link EntryDecoder} for decoding entries as a {@link Map}.
     *
     * @param keyDecoder         the {@link Decoder} for decoding keys
     * @param valueDecoder       the {@link Decoder} for decoding values
     * @param factory            the factory for creating a {@link Map}
     * @param allowDuplicatedKey whether to allow duplicate keys
     * @param finalizer          the finalizer for the {@link Map}
     * @param <K>                the type of the key
     * @param <V>                the type of the value
     * @return a {@link EntryDecoder} for decoding entries as a {@link Map}
     */
    static <K, V> @NotNull Decoder<Map<K, V>> map(@NotNull Decoder<K> keyDecoder, @NotNull Decoder<V> valueDecoder,
                                                  @NotNull Supplier<Map<K, V>> factory, boolean allowDuplicatedKey,
                                                  @NotNull UnaryOperator<Map<K, V>> finalizer) {
        Objects.requireNonNull(keyDecoder);
        Objects.requireNonNull(valueDecoder);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(finalizer);
        MapEntryDecodeProcessor<K, V> processor = new MapEntryDecodeProcessor<>(keyDecoder, valueDecoder, factory, allowDuplicatedKey, finalizer);
        return (EntryDecoder<K, V, Map<K, V>>) () -> processor;
    }

    /**
     * Returns the {@link EntryDecoder.DecodeProcessor} for decoding entries and collect them.
     *
     * @return the {@link EntryDecoder.DecodeProcessor}
     */
    @NotNull EntryDecoder.DecodeProcessor<K, V, R> decodeProcessor();

    @Override
    default @NotNull Result<R, DecodeError> decode(@NotNull In in) {
        return in.readMap(this.decodeProcessor().createIdentity(), (identity, entryIn) -> {
            Result<K, DecodeError> keyResult = this.decodeProcessor().decodeKey(entryIn.keyIn());
            Result<V, DecodeError> valueResult = this.decodeProcessor().decodeValue(entryIn.valueIn());

            if (keyResult.isFailure()) {
                return keyResult.unwrapError().isIgnorable() ? Result.success() : keyResult.asFailure();
            }

            if (valueResult.isFailure()) {
                return valueResult.unwrapError().isIgnorable() ? Result.success() : valueResult.asFailure();
            }

            K key = keyResult.unwrap();
            V value = valueResult.unwrap();
            Result<Void, DecodeError> acceptResult = this.decodeProcessor().acceptEntry(identity, key, value);

            if (acceptResult.isFailure() && !acceptResult.unwrapError().isIgnorable()) {
                return acceptResult.asFailure();
            }

            return Result.success();
        }).map(this.decodeProcessor()::finalizeIdentity);
    }

    /**
     * An interface to processing entries of the collection.
     *
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @param <T> the type of the collection to decode entries to
     */
    interface DecodeProcessor<K, V, T> {

        /**
         * Decodes a key from the provided input source.
         *
         * @param in the {@link In} for reading the key to decode
         * @return a result containing the decoded key if the operation is successful, or a {@link DecodeError} if the decoding process fails
         */
        @NotNull Result<K, DecodeError> decodeKey(@NotNull In in);

        /**
         * Decodes a value from the provided input source.
         *
         * @param in the {@link In} for reading the value to decode
         * @return a result containing the decoded value if the operation is successful, or a {@link DecodeError} if the decoding process fails
         */
        @NotNull Result<V, DecodeError> decodeValue(@NotNull In in);

        /**
         * Creates a {@link T} for an identity of {@link In#readMap(Object, java.util.function.BiFunction)}.
         *
         * @return a {@link T} for an identity of {@link In#readMap(Object, java.util.function.BiFunction)}
         */
        @NotNull T createIdentity();

        /**
         * Accepts the key-value entry to the identity object of {@link T}.
         *
         * @param identity the identity object
         * @param key      the key
         * @param value    the value
         * @return the {@link Result} of accepting the entry
         */
        @NotNull Result<Void, DecodeError> acceptEntry(@NotNull T identity, @UnknownNullability K key, @UnknownNullability V value);

        /**
         * Finalizes the identity object of {@link T}.
         *
         * @param identity the identity object
         * @return the final {@link T} object
         */
        @NotNull T finalizeIdentity(@NotNull T identity);

    }
}
