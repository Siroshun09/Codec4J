package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;

/**
 * An interface to processing entries of the collection.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @param <E> the type of the key-value entry
 * @param <T> the type of the collection
 */
public interface EntryProcessor<K, V, E, T> {

    /**
     * Encodes the key of the entry to the provided {@link Out}.
     *
     * @param out   the {@link Out} for writing the encoded key
     * @param entry the key-value entry to encode to the {@link Out}
     * @param <O>   the type of the output destination
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    <O> @NotNull Result<O, EncodeError> encodeKey(@NotNull Out<O> out, @UnknownNullability E entry);

    /**
     * Encodes the value of the given entry to the provided {@link Out}.
     *
     * @param out   the {@link Out} instance for writing the encoded value
     * @param entry the key-value entry whose value is to be encoded
     * @param <O>   the type of the output destination
     * @return a result containing the output destination if the operation is successful, or an {@link EncodeError} if the operation fails
     */
    <O> @NotNull Result<O, EncodeError> encodeValue(@NotNull Out<O> out, @UnknownNullability E entry);

    /**
     * Creates an {@link Iterator} from {@link T} for encoding entries.
     *
     * @param input the object to encode
     * @return the {@link Iterator} of entries
     */
    @NotNull Iterator<E> toEntryIterator(@UnknownNullability T input);

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
