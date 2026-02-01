package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for reading entries from an {@link In}.
 */
public interface EntryReader {

    /**
     * Checks if there is a next entry.
     * <p>
     * This method returns {@code false} if the check is failed.
     *
     * @return {@code true} if there is a next entry, otherwise {@code false}
     */
    boolean hasNext();

    /**
     * Reads the next entry.
     *
     * @return a result containing the next entry, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<EntryIn, DecodeError> next();

    /**
     * Finishes reading entries.
     *
     * @return a result containing {@code null} if the operation succeeded, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Void, DecodeError> finish();

}
