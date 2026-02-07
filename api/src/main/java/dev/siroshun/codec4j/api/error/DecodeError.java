package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

/**
 * An interface to indicate errors when decoding.
 */
@NotNullByDefault
public sealed interface DecodeError permits DecodeError.Failure, DecodeError.FatalError, DecodeError.IgnorableError, DecodeError.InvalidChar, DecodeError.InvalidNumber, DecodeError.InvalidNumberFormat, DecodeError.MultipleError, DecodeError.NoElementError, DecodeError.NoEntryError, DecodeError.TypeMismatch {

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
     * @param expectedType the expected {@link Type.NumberValue}
     * @param actualValue  the actual {@link Number} value
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
     * @param e the caused {@link NumberFormatException}
     * @return a new {@link DecodeError}
     */
    static InvalidNumberFormat invalidNumberFormat(NumberFormatException e) {
        Objects.requireNonNull(e);
        return new DecodeErrors.InvalidNumberFormatError(e);
    }

    /**
     * Creates a {@link DecodeError} when there is no element.
     *
     * @return a new {@link DecodeError}
     */
    static NoElementError noElementError() {
        return new DecodeErrors.NoElementError(OptionalInt.empty());
    }

    /**
     * Creates a {@link DecodeError} when there is no element.
     *
     * @return a new {@link DecodeError}
     */
    static NoElementError noElementError(int index) {
        return new DecodeErrors.NoElementError(OptionalInt.of(index));
    }

    /**
     * Creates a {@link DecodeError} when there is no entry.
     *
     * @return a new {@link DecodeError}
     */
    static NoEntryError noEntryError() {
        return new DecodeErrors.NoEntryError();
    }

    /**
     * Creates a {@link DecodeError} when fatal error occurred while decoding.
     *
     * @param cause the {@link Throwable} that caused the error
     * @return a new {@link DecodeError}
     */
    static FatalError fatalError(Throwable cause) {
        Objects.requireNonNull(cause);
        return new DecodeErrors.FatalError(cause);
    }

    /**
     * Creates a {@link DecodeError} that may have multiple {@link DecodeError}s.
     *
     * @param errors the {@link List} of {@link DecodeError}s
     * @return a new {@link DecodeError} that may have multiple {@link DecodeError}s
     */
    static MultipleError multipleError(List<DecodeError> errors) {
        Objects.requireNonNull(errors);
        return new DecodeErrors.MultipleError(List.copyOf(errors));
    }

    /**
     * Creates a {@link Failure} error with no additional information.
     *
     * @return a {@link Failure} error
     */
    static Failure failure() {
        return new DecodeErrors.Failure("");
    }

    /**
     * Creates a {@link Failure} error with a message.
     *
     * @param message a message that is used in {@link Object#toString()} for printing debug logs
     * @return a {@link Failure} error with a message
     */
    static Failure failure(String message) {
        Objects.requireNonNull(message);
        return new DecodeErrors.Failure(message);
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
     * Makes this {@link DecodeError} ignorable.
     *
     * @return {@link IgnorableError} with this {@link DecodeError}
     */
    default IgnorableError asIgnorable() {
        return new DecodeErrors.IgnorableError(this);
    }

    /**
     * Returns whether this {@link DecodeError} can be ignored.
     *
     * @return {@code true} if this {@link DecodeError} can be ignored, otherwise {@code false}
     */
    default boolean isIgnorable() {
        return this instanceof IgnorableError;
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
     * An interface that indicates a {@link DecodeError} when there is no element.
     */
    sealed interface NoElementError extends DecodeError permits DecodeErrors.NoElementError {

        /**
         * Gets an index of the element.
         * <p>
         * If this error does not have an index, returns {@link OptionalInt#empty()}.
         *
         * @return an index of the element
         */
        OptionalInt index();

    }

    /**
     * An interface that indicates a {@link DecodeError} when there is no entry.
     */
    sealed interface NoEntryError extends DecodeError permits DecodeErrors.NoEntryError {
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
     * An interface that may have multiple {@link DecodeError}s.
     */
    sealed interface MultipleError extends DecodeError permits DecodeErrors.MultipleError {

        /**
         * Gets the {@link List} of {@link DecodeError}s.
         *
         * @return the {@link List} of {@link DecodeError}s
         */
        List<DecodeError> errors();

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
