package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.decoder.collection.ListDecoder;
import dev.siroshun.codec4j.api.encoder.collection.ListEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ListCodec {

    /**
     * Creates a {@link Codec} for {@link List} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link Codec}
     */
    public static <E> @NotNull Codec<List<E>> create(@NotNull Codec<E> elementCodec) {
        Objects.requireNonNull(elementCodec);
        return Codec.codec(ListEncoder.create(elementCodec), ListDecoder.create(elementCodec)).named("ListCodec[" + elementCodec + "]");
    }

    private ListCodec() {
        throw new UnsupportedOperationException();
    }
}
