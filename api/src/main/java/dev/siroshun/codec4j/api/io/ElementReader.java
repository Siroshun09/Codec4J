package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for reading elements from an {@link In}.
 *
 * @param <I> the type of {@link In}
 */
public interface ElementReader<I extends In> {

    /**
     * Checks if there is a next element.
     * <p>
     * This method returns {@code false} if the check is failed.
     *
     * @return {@code true} if there is a next element, otherwise {@code false}
     */
    boolean hasNext();

    /**
     * Reads the next element.
     *
     * @return a result containing the next element, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<I, DecodeError> next();

    /**
     * Finishes reading elements.
     *
     * @return a result containing {@code null} if the operation succeeded, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Void, DecodeError> finish();

}
