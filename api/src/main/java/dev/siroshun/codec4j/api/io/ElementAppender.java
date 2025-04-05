package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An interface for writing elements to an {@link Out}.
 *
 * @param <O> the type of the output destination
 */
public interface ElementAppender<O> {

    /**
     * Appends an element to the output destination.
     *
     * @param function the function to write the element
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> append(@NotNull Function<@NotNull Out<O>, @NotNull Result<O, EncodeError>> function);

    /**
     * Finishes writing elements to the output destination.
     *
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> finish();

}
