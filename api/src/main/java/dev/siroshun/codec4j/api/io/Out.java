package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

/**
 * An interface for writing and encoding data to various destinations.
 * <p>
 * This interface provides methods to write primitive types, strings, lists, and maps
 * to an underlying data destination. All methods return a {@link Result} that either
 * contains the successfully encoded value or an {@link EncodeError} if the operation failed.
 *
 * @param <O> the type of the output destination
 */
public interface Out<O> {

    /**
     * Writes a {@link Boolean} value to the output destination.
     *
     * @param value the {@link Boolean} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeBoolean(boolean value);

    /**
     * Writes a {@link Byte} value to the output destination.
     *
     * @param value the {@link Byte} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeByte(byte value);

    /**
     * Writes a {@link Character} value to the output destination.
     *
     * @param value the {@link Character} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeChar(char value);

    /**
     * Writes a {@link Double} value to the output destination.
     *
     * @param value the {@link Double} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeDouble(double value);

    /**
     * Writes a {@link Float} value to the output destination.
     *
     * @param value the {@link Float} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeFloat(float value);

    /**
     * Writes an {@link Integer} value to the output destination.
     *
     * @param value the {@link Integer} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeInt(int value);

    /**
     * Writes a {@link Long} value to the output destination.
     *
     * @param value the {@link Long} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeLong(long value);

    /**
     * Writes a {@link Short} value to the output destination.
     *
     * @param value the {@link Short} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeShort(short value);

    /**
     * Writes a {@link String} value to the output destination.
     *
     * @param value the {@link String} value to write
     * @return a result containing the output destination, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<O, EncodeError> writeString(@NotNull String value);

    /**
     * Creates a new list in the output destination.
     *
     * @return a result containing an {@link ElementAppender} for adding elements to the list, or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<ElementAppender<O>, EncodeError> createList();

    /**
     * Creates a new map in the output destination.
     *
     * @return a result containing an {@link EntryAppender} for adding key-value pairs to the map,or an {@link EncodeError} if the operation failed
     */
    @NotNull Result<EntryAppender<O>, EncodeError> createMap();

}
