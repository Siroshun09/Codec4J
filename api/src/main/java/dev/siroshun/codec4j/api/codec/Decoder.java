package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for decoding data from an {@link In}.
 *
 * @param <T> the type of the decoded data
 */
public interface Decoder<T> {

    /**
     * Decodes the data from the provided {@link In}.
     *
     * @param in the {@link In} for reading the data to decode
     * @return a result containing the decoded data, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<T, DecodeError> decode(@NotNull In in);

}
