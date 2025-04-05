package dev.siroshun.codec4j.api.io;

import org.jetbrains.annotations.NotNull;

/**
 * An interface for reading key-value from an {@link In}.
 */
public interface EntryIn {

    /**
     * Gets the {@link In} for reading the key.
     *
     * @return the {@link In} for reading the key
     */
    @NotNull In keyIn();

    /**
     * Gets the {@link In} for reading the value.
     *
     * @return the {@link In} for reading the value
     */
    @NotNull In valueIn();

}
