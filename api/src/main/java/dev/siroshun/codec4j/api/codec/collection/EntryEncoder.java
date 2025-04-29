package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Encoder;
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
     * Returns the {@link EntryProcessor} for extracting entries from {@link T} and decoding them.
     *
     * @return the {@link EntryProcessor}
     */
    @NotNull EntryProcessor<?, ?, E, T> processor();

    @Override
    default <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Result<EntryAppender<O>, EncodeError> appenderResult = out.createMap();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        EntryAppender<O> appender = appenderResult.unwrap();
        Iterator<E> iterator = this.processor().toEntryIterator(input);

        while (iterator.hasNext()) {
            E entry = iterator.next();
            Result<Void, EncodeError> entryResult = appender.append(keyOut -> this.processor().encodeKey(keyOut, entry), valueOut -> this.processor().encodeValue(valueOut, entry));

            if (entryResult.isFailure()) {
                return entryResult.asFailure();
            }
        }

        return appender.finish();
    }
}
