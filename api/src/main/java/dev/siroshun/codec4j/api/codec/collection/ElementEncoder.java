package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Encoder} for encoding elements to an {@link Out}.
 *
 * @param <E> the type of the element
 * @param <T> the type of the source collection to encode
 */
public interface ElementEncoder<E, T> extends Encoder<T> {

    /**
     * Returns the {@link ElementProcessor} for extracting elements from {@link T} and decoding them.
     *
     * @return the {@link ElementProcessor}
     */
    @NotNull ElementProcessor<E, T> processor();

    @Override
    default <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Objects.requireNonNull(out);
        Objects.requireNonNull(input);

        Result<ElementAppender<O>, EncodeError> appenderResult = out.createList();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        ElementAppender<O> appender = appenderResult.unwrap();
        Iterator<E> iterator = this.processor().toIterator(input);

        while (iterator.hasNext()) {
            E element = iterator.next();
            Result<O, EncodeError> elementResult = appender.append(elementOut -> this.processor().encodeElement(elementOut, element));

            if (elementResult.isFailure()) {
                return elementResult.asFailure();
            }
        }

        return appender.finish();
    }
}
