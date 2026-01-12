package dev.siroshun.codec4j.api.decoder;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * An interface for decoding data from an {@link In}.
 *
 * @param <T> the type of the decoded data
 */
public interface Decoder<T> {

    /**
     * Decodes the data from the provided {@link In}.
     *
     * @param in the {@link In} for reading the data to decode
     * @return a result containing the decoded data, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<T, DecodeError> decode(@NotNull In in);

    /**
     * Maps the decoded data to another type.
     *
     * @param mapper the {@link Function} to map the decoded data to another type
     * @param <A>    the type of the mapped data
     * @return a new {@link Decoder} that maps the decoded data to another type
     */
    default <A> @NotNull Decoder<A> map(@NotNull Function<? super T, ? extends A> mapper) {
        Objects.requireNonNull(mapper);
        return in -> Decoder.this.decode(in).map(mapper);
    }

    /**
     * Maps the decoded data to another type and flattens the {@link Result}.
     *
     * @param mapper the {@link Function} to map the decoded data to another type and returns a {@link Result} of the mapping
     * @param <A>    the type of the mapped data
     * @return a new {@link Decoder} that maps the decoded data to another type and flattens the {@link Result}
     */
    default <A> @NotNull Decoder<A> flatMap(@NotNull Function<? super T, Result<A, DecodeError>> mapper) {
        Objects.requireNonNull(mapper);
        return in -> Decoder.this.decode(in).flatMap(mapper);
    }

    /**
     * Inspects the decoded data.
     *
     * @param inspector the {@link Function} to inspect the decoded data and returns a {@link Result} of the inspection
     * @return a new {@link Decoder} that inspects the decoded data
     */
    default @NotNull Decoder<T> inspect(@NotNull Function<? super T, Result<T, DecodeError>> inspector) {
        Objects.requireNonNull(inspector);
        return in -> Decoder.this.decode(in).flatMap(inspector);
    }

    /**
     * Catches a {@link DecodeError} and returns a new {@link Result}.
     *
     * @param onError the {@link Function} to catch the {@link DecodeError} and returns a {@link Result} of the new {@link Result}
     * @return a new {@link Decoder} that catches a {@link DecodeError} and returns a new {@link Result}
     */
    default @NotNull Decoder<T> catchError(@NotNull Function<? super DecodeError, Result<T, DecodeError>> onError) {
        Objects.requireNonNull(onError);
        return in -> Decoder.this.decode(in).flatMapError(onError);
    }
}
