package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Decoder} for decoding elements from an {@link In#readList(Object, java.util.function.BiFunction)}.
 *
 * @param <E> the type of the decoded elements
 * @param <R> the type of the decoded data
 */
public interface ElementDecoder<E, R> extends Decoder<R> {

    /**
     * Returns the {@link ElementProcessor} for decoding elements and collect them.
     *
     * @return the {@link ElementProcessor}
     */
    @NotNull ElementProcessor<E, R> processor();

    @Override
    default @NotNull Result<R, DecodeError> decode(In in) {
        return in.readList(this.processor().createIdentity(), (identity, e) -> {
            Result<E, DecodeError> decodeResult = this.processor().decodeElement(e);

            if (decodeResult.isFailure()) {
                return decodeResult.unwrapError() instanceof DecodeError.IgnorableError ? Result.success() : decodeResult.asFailure();
            }

            E element = decodeResult.unwrap();
            Result<Void, DecodeError> acceptResult = this.processor().acceptElement(identity, element);

            if (acceptResult.isFailure()) {
                return acceptResult.asFailure();
            }

            return Result.success();
        }).map(this.processor()::finalizeIdentity);
    }
}
