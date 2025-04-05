package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Objects;

/**
 * An interface to indicate errors when decoding.
 */
@NotNullByDefault
public sealed interface DecodeError permits DecodeError.Failure, DecodeError.FatalError, DecodeError.IgnorableError, DecodeError.InvalidChar, DecodeError.InvalidNumber, DecodeError.InvalidNumberFormat, DecodeError.IterationError, DecodeError.TypeMismatch {

    /**
     * Creates a {@link DecodeError} when the type is different from the expected type.
     *
     * @param expected the expected {@link Type}
     * @param actual   the actual {@link Type}
     * @return a new {@link DecodeError}
     */
    static TypeMismatch typeMismatch(Type expected, Type actual) {
        Objects.requireNonNull(expected);
        Objects.requireNonNull(actual);
        return new DecodeErrors.TypeMismatchError(expected, actual);
    }

    /**
     * Creates a {@link DecodeError} when the data is invalid {@link Character}.
     *
     * @param actualValue the actual {@link String}
     * @return a new {@link DecodeError}
     */
    static InvalidChar invalidChar(String actualValue) {
        Objects.requireNonNull(actualValue);
        return new DecodeErrors.InvalidCharError(actualValue);
    }

    /**
     * Creates a {@link DecodeError} when the data is invalid number.
     *
     * @return a new {@link DecodeError}
     */
    static InvalidNumber invalidNumber(Type.NumberValue<?> expectedType, Number actualValue) {
        Objects.requireNonNull(expectedType);
        Objects.requireNonNull(actualValue);
        return new DecodeErrors.InvalidNumberError(expectedType, actualValue);
    }

    /**
     * Creates a {@link DecodeError} when the data is an invalid number format.
     *
     * @return a new {@link DecodeError}
     */
    static InvalidNumberFormat invalidNumberFormat(NumberFormatException e) {
        Objects.requireNonNull(e);
        return new DecodeErrors.InvalidNumberFormatError(e);
    }

    /**
     * Creates a {@link DecodeError} when {@link Result.Failure} is returned when iterating elements.
     *
     * @param cause the {@link Result.Failure}
     * @return a new {@link DecodeError}
     */
    static IterationError iterationError(Result.Failure<Void, ?> cause) {
        Objects.requireNonNull(cause);
        return new DecodeErrors.IterationError(cause);
    }

    /**
     * Creates a {@link DecodeError} when fatal error occurred while decoding.
     *
     * @return a new {@link DecodeError}
     */
    static FatalError fatalError(Throwable cause) {
        Objects.requireNonNull(cause);
        return new DecodeErrors.FatalError(cause);
    }

    /**
     * Converts this {@link DecodeError} to {@link Result.Failure}.
     *
     * @param <T> any type
     * @return the {@link Result.Failure}
     */
    default <T> Result.Failure<T, DecodeError> asFailure() {
        return Result.failure(this);
    }

    /**
     * Makes this {@link DecodeError} ignorable
     *
     * @return {@link IgnorableError} with this {@link DecodeError}
     */
    default IgnorableError asIgnorable() {
        return new DecodeErrors.IgnorableError(this);
    }

    /**
     * An interface that indicates a {@link DecodeError} when the type is different from the expected type.
     */
    sealed interface TypeMismatch extends DecodeError permits DecodeErrors.TypeMismatchError {

        /**
         * Gets an expected {@link Type}.
         *
         * @return an expected {@link Type}
         */
        Type expectedType();

        /**
         * Gets an actual {@link Type}.
         *
         * @return an actual {@link Type}
         */
        Type actualType();
    }

    /**
     * An interface that indicates a {@link DecodeError} when the data is invalid {@link Character}.
     */
    sealed interface InvalidChar extends DecodeError permits DecodeErrors.InvalidCharError {
        /**
         * Gets an actual {@link String}.
         *
         * @return an actual {@link String}
         */
        String actualValue();
    }

    /**
     * An interface that indicates a {@link DecodeError} when the data is invalid number.
     */
    sealed interface InvalidNumber extends DecodeError permits DecodeErrors.InvalidNumberError {

        /**
         * Gets an expected {@link Type.NumberValue}.
         *
         * @return an expected {@link Type.NumberValue}
         */
        Type.NumberValue<?> expectedType();

        /**
         * Gets an actual {@link Number} value.
         *
         * @return an actual {@link Number}
         */
        Number actualValue();
    }

    /**
     * An interface that indicates a {@link DecodeError} when the data is an invalid number format.
     */
    sealed interface InvalidNumberFormat extends DecodeError permits DecodeErrors.InvalidNumberFormatError {

        /**
         * Gets a caused {@link NumberFormatException}.
         *
         * @return a caused {@link NumberFormatException}
         */
        NumberFormatException exception();
    }

    /**
     * An interface that indicates a {@link DecodeError} when {@link Result.Failure} is returned when iterating elements.
     */
    sealed interface IterationError extends DecodeError permits DecodeErrors.IterationError {
        /**
         * Gets a {@link Result.Failure} that is returned when iterating elements.
         *
         * @return a {@link Result.Failure} that is returned when iterating elements
         */
        Result.Failure<Void, ?> cause();
    }

    /**
     * An interface that indicates a {@link DecodeError} when fatal error occurred while decoding.
     */
    sealed interface FatalError extends DecodeError permits DecodeErrors.FatalError {

        /**
         * Gets a {@link Throwable} that caused the error.
         *
         * @return a {@link Throwable} that caused the error
         */
        Throwable cause();
    }

    /**
     * An interface that marks a {@link DecodeError} as ignorable.
     */
    sealed interface IgnorableError extends DecodeError permits DecodeErrors.IgnorableError {
        /**
         * Gets an original {@link DecodeError}.
         *
         * @return an original {@link DecodeError}
         */
        DecodeError error();
    }

    /**
     * An interface that indicates a {@link DecodeError} for custom error type.
     */
    non-sealed interface Failure extends DecodeError {
    }
}
