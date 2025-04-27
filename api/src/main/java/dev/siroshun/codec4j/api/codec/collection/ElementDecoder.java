package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.BiFunction;

/**
 * A {@link Decoder} for decoding elements from an {@link In#readList(Object, BiFunction)}.
 *
 * @param <E> the type of the decoded elements
 * @param <R> the type of the decoded data
 */
public interface ElementDecoder<E, R> extends Decoder<R> {

    /**
     * Decodes an element from the provided {@link In}.
     *
     * @param in the {@link In} for reading the element to decode
     * @return a result containing the decoded element, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<E, DecodeError> decodeElement(@NotNull In in);

    /**
     * Creates an identity object for the {@link R} type.
     *
     * @return an identity object for the {@link R} type
     */
    @NotNull R createIdentity();

    /**
     * Accepts an element and returns a {@link Result} of the acceptance.
     *
     * @param identity the identity object for the {@link R} type
     * @param element  the decoded element to accept
     * @return a result containing {@code null} if the operation succeeded, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Void, DecodeError> acceptElement(@NotNull R identity, @UnknownNullability E element);

    /**
     * Finalizes the identity object.
     *
     * @param identity the identity object for the {@link R} type to finalize
     * @return the finalized identity object for the {@link R} type
     */
    @NotNull R finalizeIdentity(@NotNull R identity);

    @Override
    default @NotNull Result<R, DecodeError> decode(In in) {
        return in.readList(this.createIdentity(), (identity, e) -> {
            Result<E, DecodeError> decodeResult = this.decodeElement(e);

            if (decodeResult.isFailure()) {
                return decodeResult.unwrapError() instanceof DecodeError.IgnorableError ? Result.success() : decodeResult.asFailure();
            }

            E element = decodeResult.unwrap();
            Result<Void, DecodeError> acceptResult = this.acceptElement(identity, element);

            if (this.acceptElement(identity, element).isFailure()) {
                return acceptResult.asFailure();
            }

            return Result.success();
        }).map(this::finalizeIdentity);
    }
}
