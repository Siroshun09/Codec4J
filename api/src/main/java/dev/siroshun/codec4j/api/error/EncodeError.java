package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;
import java.util.Objects;

/**
 * An interface to indicate errors when encoding.
 */
@NotNullByDefault
public sealed interface EncodeError permits EncodeError.Failure, EncodeError.FatalError, EncodeError.MultipleError, EncodeError.NotWritableType {

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
     * Creates a {@link EncodeError} that may have multiple {@link EncodeError}s.
     *
     * @param errors the {@link List} of {@link EncodeError}s
     * @return a new {@link EncodeError} that may have multiple {@link EncodeError}s
     */
    static MultipleError multipleError(List<EncodeError> errors) {
        Objects.requireNonNull(errors);
        return new EncodeErrors.MultipleError(List.copyOf(errors));
    }

    /**
     * Creates a {@link Failure} error with no additional information.
     *
     * @return a {@link Failure} error
     */
    static Failure failure() {
        return new EncodeErrors.Failure("");
    }

    /**
     * Creates a {@link Failure} with a message.
     *
     * @param message a message that is used in {@link Object#toString()} for printing debug logs
     * @return a {@link Failure} error with a message
     */
    static Failure failure(String message) {
        Objects.requireNonNull(message);
        return new EncodeErrors.Failure(message);
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
     * An interface that may have multiple {@link EncodeError}s.
     */
    sealed interface MultipleError extends EncodeError permits EncodeErrors.MultipleError {

        /**
         * Gets the {@link List} of {@link EncodeError}s.
         *
         * @return the {@link List} of {@link EncodeError}s
         */
        List<EncodeError> errors();

    }

    /**
     * An interface that indicates a {@link EncodeError} for custom error type.
     */
    non-sealed interface Failure extends EncodeError {
    }
}
