package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.EntryReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * A {@link Decoder} for decoding entries from an {@link In#readMap()}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @param <R> the type of the source collection to decode
 */
public interface EntryDecoder<K, V, R> extends Decoder<R> {

    /**
     * Returns the {@link EntryDecoder.DecodeProcessor} for decoding entries and collect them.
     *
     * @return the {@link EntryDecoder.DecodeProcessor}
     */
    @NotNull EntryDecoder.DecodeProcessor<K, V, R> decodeProcessor();

    @Override
    default @NotNull Result<R, DecodeError> decode(@NotNull In in) {
        Result<EntryReader, DecodeError> readerResult = in.readMap();
        if (readerResult.isFailure()) {
            return readerResult.asFailure();
        }

        EntryReader reader = readerResult.unwrap();
        R identity = this.decodeProcessor().createIdentity();

        while (reader.hasNext()) {
            Result<EntryIn, DecodeError> entryResult = reader.next();

            if (entryResult.isFailure()) {
                return entryResult.asFailure();
            }

            EntryIn entryIn = entryResult.unwrap();

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
        }

        Result<Void, DecodeError> finishResult = reader.finish();
        if (finishResult.isFailure()) {
            return finishResult.asFailure();
        }

        return Result.success(this.decodeProcessor().finalizeIdentity(identity));
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
         * Creates an identity object of {@link T}.
         *
         * @return an identity object of {@link T}
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
