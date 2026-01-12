package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.error.DecodeError;
import org.jetbrains.annotations.UnknownNullability;

/**
 * A {@link DecodeError} when an entry is duplicated during decoding into a map that does not allow duplicates.
 *
 * @param key   the duplicated key
 * @param value the value of the duplicated key
 */
public record DuplicatedKeyError(@UnknownNullability Object key,
                                 @UnknownNullability Object value) implements DecodeError.Failure {
}
