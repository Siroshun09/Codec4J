package dev.siroshun.codec4j.api.encoder.collection;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.io.Out;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ListEncoder {

    /**
     * Creates a new {@link ElementEncoder} that encodes the collection of {@link E} to an {@link Out}.
     *
     * @param <C> the type of the collection to encode
     * @return a new {@link ElementEncoder} that encodes the collection of {@link E} to an {@link Out}
     */
    public static <E, C extends Iterable<E>> @NotNull Encoder<C> create(@NotNull Encoder<E> elementEncoder) {
        Objects.requireNonNull(elementEncoder);
        ElementEncoder.EncodeProcessor<E, C> processor = new IterableEncodeProcessor<>(elementEncoder);
        return (ElementEncoder<E, C>) () -> processor;
    }

    private ListEncoder() {
        throw new UnsupportedOperationException();
    }
}
