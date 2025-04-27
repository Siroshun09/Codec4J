package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

/**
 * An {@link Encoder} for encoding elements to an {@link Out}.
 *
 * @param <E> the type of the element to encode
 * @param <T> the type of the iterable to encode
 */
public interface ElementEncoder<E, T extends Iterable<E>> extends Encoder<T> {

    /**
     * Encodes an element to the provided {@link Out}.
     *
     * @param out     the {@link Out} for writing the encoded element
     * @param element the element to encode to the {@link Out}
     * @param <O>     the type of the output destination
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    <O> @NotNull Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element);

    @Override
    default <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Objects.requireNonNull(out);
        Objects.requireNonNull(input);

        Result<ElementAppender<O>, EncodeError> appenderResult = out.createList();

        if (appenderResult.isFailure()) {
            return appenderResult.asFailure();
        }

        ElementAppender<O> appender = appenderResult.unwrap();

        for (E element : input) {
            Result<O, EncodeError> elementResult = appender.append(elementOut -> this.encodeElement(elementOut, element));

            if (elementResult.isFailure()) {
                return elementResult.asFailure();
            }
        }

        return appender.finish();
    }
}
