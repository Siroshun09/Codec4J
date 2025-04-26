package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * An interface for writing key-value to an {@link Out}.
 *
 * @param <O> the type of the output destination
 */
public interface EntryAppender<O> {

    /**
     * Appends a key-value pair to the output destination.
     *
     * @param keyWriter   the function to write the key
     * @param valueWriter the function to write the value
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    @NotNull Result<Void, EncodeError> append(@NotNull Function<@NotNull Out<O>, @NotNull Result<O, EncodeError>> keyWriter, @NotNull Function<@NotNull Out<O>, @NotNull Result<O, EncodeError>> valueWriter);

    /**
     * Finishes writing key-value pairs to the output destination.
     *
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> finish();

}
