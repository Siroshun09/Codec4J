package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Objects;

/**
 * An interface to indicate errors when encoding.
 */
@NotNullByDefault
public sealed interface EncodeError permits EncodeError.Failure, EncodeError.FatalError, EncodeError.NotWritableType {

    /**
     * Creates a {@link EncodeError} when the type is not writable.
     *
     * @param type the {@link Type} that is not writable
     * @return a new {@link EncodeError}
     */
    static NotWritableType notWritableType(Type type) {
        Objects.requireNonNull(type);
        return new EncodeErrors.NotWritableTypeError(type);
    }

    /**
     * Creates a {@link EncodeError} when a fatal error occurred while encoding.
     *
     * @param cause the {@link Throwable} that caused the error
     * @return a new {@link EncodeError}
     */
    static FatalError fatalError(Throwable cause) {
        Objects.requireNonNull(cause);
        return new EncodeErrors.FatalError(cause);
    }

    /**
     * Converts this {@link EncodeError} to {@link Result.Failure}.
     *
     * @param <T> any type
     * @return the {@link Result.Failure}
     */
    default <T> Result.Failure<T, EncodeError> asFailure() {
        return Result.failure(this);
    }

    /**
     * An interface that indicates a {@link EncodeError} when the type is not writable.
     */
    sealed interface NotWritableType extends EncodeError permits EncodeErrors.NotWritableTypeError {
        /**
         * Gets the {@link Type} that is not writable.
         *
         * @return the {@link Type} that is not writable
         */
        Type type();
    }

    /**
     * An interface that indicates a {@link EncodeError} when a fatal error occurred while encoding.
     */
    sealed interface FatalError extends EncodeError permits EncodeErrors.FatalError {
        /**
         * Gets the {@link Throwable} that caused the error.
         *
         * @return the {@link Throwable} that caused the error
         */
        Throwable cause();
    }

    /**
     * An interface that indicates a {@link EncodeError} for custom error type.
     */
    non-sealed interface Failure extends EncodeError {
    }
}
