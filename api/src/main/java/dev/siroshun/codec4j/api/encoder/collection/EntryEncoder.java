package dev.siroshun.codec4j.api.encoder.collection;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;

/**
 * An {@link Encoder} for encoding entries to an {@link Out}.
 *
 * @param <E> the type of the entry
 * @param <T> the type of the source collection to encode
 */
public interface EntryEncoder<E, T> extends Encoder<T> {

    /**
     * Returns the {@link EntryEncoder.EncodeProcessor} for extracting entries from {@link T} and decoding them.
     *
     * @return the {@link EntryEncoder.EncodeProcessor}
     */
    @NotNull EntryEncoder.EncodeProcessor<E, T> encodeProcessor();

    @Override
    default <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Result<EntryAppender<O>, EncodeError> appenderResult = out.createMap();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        EntryAppender<O> appender = appenderResult.unwrap();
        Iterator<E> iterator = this.encodeProcessor().toEntryIterator(input);

        while (iterator.hasNext()) {
            E entry = iterator.next();
            Result<Void, EncodeError> entryResult = appender.append(keyOut -> this.encodeProcessor().encodeKey(keyOut, entry), valueOut -> this.encodeProcessor().encodeValue(valueOut, entry));

            if (entryResult.isFailure()) {
                return entryResult.asFailure();
            }
        }

        return appender.finish();
    }

    /**
     * An interface for processing entries of the collection.
     *
     * @param <E> the type of entry
     * @param <T> the type of collection
     */
    interface EncodeProcessor<E, T> {

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

    }
}
