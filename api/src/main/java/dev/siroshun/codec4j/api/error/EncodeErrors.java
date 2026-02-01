package dev.siroshun.codec4j.api.error;

import dev.siroshun.codec4j.api.io.Type;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;

@NotNullByDefault
final class EncodeErrors {

    record NotWritableTypeError(Type type) implements EncodeError.NotWritableType {
    }

    record FatalError(Throwable cause) implements EncodeError.FatalError {
    }

    record MultipleError(List<EncodeError> errors) implements EncodeError.MultipleError {
    }

    record Failure(String message) implements EncodeError.Failure {
    }
}
