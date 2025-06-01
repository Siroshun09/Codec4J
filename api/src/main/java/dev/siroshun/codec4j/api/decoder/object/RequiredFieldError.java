package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.error.DecodeError;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A {@link DecodeError} when a required field is not decoded.
 *
 * @param fieldName the name of the field that is not decoded
 */
public record RequiredFieldError(@NotNull String fieldName) implements DecodeError.Failure {

    /**
     * Constructs a new {@link RequiredFieldError}.
     *
     * @param fieldName the name of the field that is not decoded
     */
    public RequiredFieldError {
        Objects.requireNonNull(fieldName);
    }
}
