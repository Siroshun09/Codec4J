package dev.siroshun.codec4j.api.encoder;

import dev.siroshun.codec4j.api.encoder.element.ElementEncoder;
import dev.siroshun.codec4j.api.encoder.entry.EntryEncoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

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

    /**
     * Maps the input data to another type.
     *
     * @param mapper the {@link Function} to map the input data to another type
     * @param <A>    the type of the mapped data
     * @return a new {@link Encoder} that maps the input data to another type
     */
    default <A> @NotNull Encoder<A> comap(@NotNull Function<? super A, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        return new Encoder<>() {
            @Override
            public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability A input) {
                return Encoder.this.encode(out, mapper.apply(input));
            }
        };
    }

    /**
     * Maps the input data to another type and flattens the {@link Result}.
     *
     * @param mapper the {@link Function} to map the input data to another type and returns a {@link Result} of the mapping
     * @param <A>    the type of the mapped data
     * @return a new {@link Encoder} that maps the input data to another type and flattens the {@link Result}
     */
    default <A> @NotNull Encoder<A> flatComap(@NotNull Function<? super A, ? extends Result<? extends T, EncodeError>> mapper) {
        Objects.requireNonNull(mapper);
        return new Encoder<>() {
            @Override
            public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability A input) {
                return mapper.apply(input).flatMap(t -> Encoder.this.encode(out, t));
            }
        };
    }

    /**
     * Creates a new {@link ElementEncoder} that encodes the collection of {@link T} to an {@link Out}.
     *
     * @param <C> the type of the collection to encode
     * @return a new {@link ElementEncoder} that encodes the collection of {@link T} to an {@link Out}
     */
    default <C extends Iterable<T>> @NotNull Encoder<C> toCollectionEncoder() {
        return ElementEncoder.create(this);
    }

    /**
     * Creates a new {@link EntryEncoder} that encodes the {@link Map}, using this {@link Encoder} for encoding the key.
     *
     * @param valueEncoder the {@link Encoder} for encoding the value of the entry to an {@link Out}
     * @param <V>          the type of the value
     * @return a new {@link EntryEncoder} that encodes the {@link Map}
     */
    default <V> @NotNull Encoder<Map<T, V>> toMapEncoderAsKey(@NotNull Encoder<V> valueEncoder) {
        return EntryEncoder.map(this, valueEncoder);
    }

    /**
     * Creates a new {@link EntryEncoder} that encodes the {@link Map}, using this {@link Encoder} for encoding the value.
     *
     * @param keyEncoder the {@link Encoder} for encoding the key of the entry to an {@link Out}
     * @param <K>        the type of the key
     * @return a new {@link EntryEncoder} that encodes the {@link Map}
     */
    default <K> @NotNull Encoder<Map<K, T>> toMapEncoderAsValue(@NotNull Encoder<K> keyEncoder) {
        return EntryEncoder.map(keyEncoder, this);
    }

}
