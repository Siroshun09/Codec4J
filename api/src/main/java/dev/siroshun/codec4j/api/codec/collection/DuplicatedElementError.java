package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.error.DecodeError;

/**
 * A {@link DecodeError} when an element is duplicated during decoding into a collection that does not allow duplicates.
 *
 * @param element the duplicated element
 */
public record DuplicatedElementError(Object element) implements DecodeError.Failure {
}
