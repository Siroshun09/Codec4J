package dev.siroshun.codec4j.api.encoder.collection;

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
     * Returns the {@link ElementEncoder.EncodeProcessor} for extracting elements from {@link T} and decoding them.
     *
     * @return the {@link ElementEncoder.EncodeProcessor}
     */
    @NotNull ElementEncoder.EncodeProcessor<E, T> encodeProcessor();

    @Override
    default <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Objects.requireNonNull(out);
        Objects.requireNonNull(input);

        Result<ElementAppender<O>, EncodeError> appenderResult = out.createList();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        ElementAppender<O> appender = appenderResult.unwrap();
        Iterator<E> iterator = this.encodeProcessor().toIterator(input);

        while (iterator.hasNext()) {
            E element = iterator.next();
            Result<O, EncodeError> elementResult = appender.append(elementOut -> this.encodeProcessor().encodeElement(elementOut, element));

            if (elementResult.isFailure()) {
                return elementResult.asFailure();
            }
        }

        return appender.finish();
    }

    /**
     * An interface for processing elements of the collection.
     *
     * @param <E> the type of element
     * @param <C> the type of collection
     */
    interface EncodeProcessor<E, C> {

        /**
         * Encodes an element to the provided {@link Out}.
         *
         * @param out     the {@link Out} for writing the encoded element
         * @param element the element to encode to the {@link Out}
         * @param <O>     the type of the output destination
         * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
         */
        <O> @NotNull Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element);

        /**
         * Creates an {@link Iterator} from {@link C} for encoding elements.
         *
         * @param input the object to encode
         * @return the {@link Iterator} of elements
         */
        @NotNull Iterator<E> toIterator(@UnknownNullability C input);

    }
}
