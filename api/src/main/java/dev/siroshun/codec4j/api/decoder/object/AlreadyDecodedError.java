package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.error.DecodeError;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A {@link DecodeError} when a field is already decoded.
 *
 * @param fieldName the name of the field that is already decoded
 */
public record AlreadyDecodedError(@NotNull String fieldName) implements DecodeError.Failure {

    /**
     * Constructs a new {@link AlreadyDecodedError}.
     *
     * @param fieldName the name of the field that is already decoded
     */
    public AlreadyDecodedError {
        Objects.requireNonNull(fieldName);
    }
}
