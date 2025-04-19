package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * An interface for encoding data to an {@link Out}.
 *
 * @param <T> the type of the input data to encode
 */
public interface Encoder<T> {

    /**
     * Encodes the data from the provided {@link T} to the provided {@link Out}.
     *
     * @param out   the {@link Out} for writing the encoded data
     * @param input the input data to encode
     * @param <O>   the type of the output destination
     * @return a result containing the encoded data, or a {@link EncodeError} if the operation failed
     */
    <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input);

}
