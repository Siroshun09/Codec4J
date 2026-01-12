package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * A {@link Decoder} for decoding elements from an {@link In#readList(Object, java.util.function.BiFunction)}.
 *
 * @param <E> the type of the element
 * @param <R> the type of the result
 */
public interface ElementDecoder<E, R> extends Decoder<R> {

    /**
     * Returns the {@link ElementDecoder.DecodeProcessor} for decoding elements and collect them.
     *
     * @return the {@link ElementDecoder.DecodeProcessor}
     */
    @NotNull ElementDecoder.DecodeProcessor<E, R> decodeProcessor();

    @Override
    default @NotNull Result<R, DecodeError> decode(@NotNull In in) {
        return in.readList(this.decodeProcessor().createIdentity(), (identity, e) -> {
            Result<E, DecodeError> decodeResult = this.decodeProcessor().decodeElement(e);

            if (decodeResult.isFailure()) {
                return decodeResult.unwrapError().isIgnorable() ? Result.success() : decodeResult.asFailure();
            }

            E element = decodeResult.unwrap();
            Result<Void, DecodeError> acceptResult = this.decodeProcessor().acceptElement(identity, element);

            if (acceptResult.isFailure() && !acceptResult.unwrapError().isIgnorable()) {
                return acceptResult.asFailure();
            }

            return Result.success();
        }).map(this.decodeProcessor()::finalizeIdentity);
    }

    /**
     * An interface to processing elements of the collection.
     *
     * @param <E> the type of the element
     * @param <C> the type of the collection to decode elements to
     */
    interface DecodeProcessor<E, C> {

        /**
         * Decodes an element from the provided {@link In}.
         *
         * @param in the {@link In} for reading the element to decode
         * @return a result containing the decoded element, or a {@link DecodeError} if the operation failed
         */
        @NotNull Result<E, DecodeError> decodeElement(@NotNull In in);

        /**
         * Creates a {@link C} for an identity of {@link In#readList(Object, java.util.function.BiFunction)}.
         *
         * @return a {@link C} for an identity of {@link In#readList(Object, java.util.function.BiFunction)}
         */
        @NotNull C createIdentity();

        /**
         * Accepts the element to the identity object of {@link C}.
         *
         * @param identity the identity object
         * @param element  the element
         * @return the {@link Result} of accepting the element
         */
        @NotNull Result<Void, DecodeError> acceptElement(@NotNull C identity, @UnknownNullability E element);

        /**
         * Finalizes the identity object of {@link C}.
         *
         * @param identity the identity object
         * @return the final {@link C} object
         */
        @NotNull C finalizeIdentity(@NotNull C identity);

    }
}
