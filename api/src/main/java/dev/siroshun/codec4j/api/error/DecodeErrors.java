package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
final class DecodeErrors {

    record TypeMismatchError(Type expectedType, Type actualType) implements DecodeError.TypeMismatch {
    }

    record InvalidCharError(String actualValue) implements DecodeError.InvalidChar {
    }

    record InvalidNumberError(Type.NumberValue<?> expectedType,
                              Number actualValue) implements DecodeError.InvalidNumber {
    }

    record InvalidNumberFormatError(NumberFormatException exception) implements DecodeError.InvalidNumberFormat {
    }

    record IterationError(Result.Failure<?, ?> cause) implements DecodeError.IterationError {
    }

    record FatalError(Throwable cause) implements DecodeError.FatalError {
    }

    record IgnorableError(DecodeError error) implements DecodeError.IgnorableError {
    }

    record Failure(String message) implements DecodeError.Failure {
    }
}
