package dev.siroshun.codec4j.api.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * An interface for reading and decoding data from various sources.
 * <p>
 * This interface provides methods to read primitive types, strings, lists, and maps
 * from an underlying data source. All methods return a {@link Result} that either
 * contains the successfully decoded value or a {@link DecodeError} if the operation failed.
 */
public interface In {

    /**
     * Returns the type of the current value.
     *
     * @return a result containing the {@link Type} of the current value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Type, DecodeError> type();

    /**
     * Reads the current value as a {@link Boolean}.
     *
     * @return a result containing the {@link Boolean} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Boolean, DecodeError> readAsBoolean();

    /**
     * Reads the current value as a {@link Byte}.
     *
     * @return a result containing the {@link Byte} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Byte, DecodeError> readAsByte();

    /**
     * Reads the current value as a {@link Character}.
     *
     * @return a result containing the {@link Character} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Character, DecodeError> readAsChar();

    /**
     * Reads the current value as a {@link Double}.
     *
     * @return a result containing the {@link Double} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Double, DecodeError> readAsDouble();

    /**
     * Reads the current value as a {@link Float}.
     *
     * @return a result containing the {@link Float} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Float, DecodeError> readAsFloat();

    /**
     * Reads the current value as an {@link Integer}.
     *
     * @return a result containing the {@link Integer} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Integer, DecodeError> readAsInt();

    /**
     * Reads the current value as a {@link Long}.
     *
     * @return a result containing the {@link Long} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Long, DecodeError> readAsLong();

    /**
     * Reads the current value as a {@link Short}.
     *
     * @return a result containing the {@link Short} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<Short, DecodeError> readAsShort();

    /**
     * Reads the current value as a {@link String}.
     *
     * @return a result containing the {@link String} value, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<String, DecodeError> readAsString();

    /**
     * Reads the current value as a list, using the provided identity object and operator.
     *
     * @param <R>      the type of the result container
     * @param identity an object that is used while processing the list elements, and will be returned as the result
     * @param operator a function that processes each element in the list using the provided {@link In} interface
     * @return a result containing the constructed container, or a {@link DecodeError} if the operation failed
     */
    <R> @NotNull Result<R, DecodeError> readList(@NotNull R identity, @NotNull BiFunction<R, ? super In, Result<Void, ?>> operator);

    /**
     * Reads the current value as a map, using the provided identity object and operator.
     *
     * @param <R>      the type of the result container
     * @param identity an object that is used while processing the map entries, and will be returned as the result
     * @param operator a function that processes each entry in the map using the provided {@link EntryIn} interface
     * @return a result containing the constructed container, or a {@link DecodeError} if the operation failed
     */
    <R> @NotNull Result<R, DecodeError> readMap(@NotNull R identity, @NotNull BiFunction<R, ? super EntryIn, Result<Void, ?>> operator);
}
