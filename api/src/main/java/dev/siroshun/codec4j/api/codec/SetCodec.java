package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.decoder.collection.SetDecoder;
import dev.siroshun.codec4j.api.encoder.collection.ListEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public final class SetCodec {

    /**
     * Creates a {@link Codec} for {@link Set} with the specified element type {@link E}.
     * <p>
     * This {@link Codec} does not allow duplicates in the set when decoding.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link Codec}
     */
    public static <E> @NotNull Codec<Set<E>> set(@NotNull Codec<E> elementCodec) {
        Objects.requireNonNull(elementCodec);
        return Codec.codec(ListEncoder.create(elementCodec), SetDecoder.create(elementCodec)).named("SetCodec[" + elementCodec + "]");
    }

    /**
     * Creates a {@link Codec} for {@link Set} with the specified element type {@link E}.
     *
     * @param elementCodec    the {@link Codec} for elements
     * @param allowDuplicates whether to allow duplicates in the set
     * @param <E>             the type of the element
     * @return a {@link Codec}
     */
    public static <E> @NotNull Codec<Set<E>> set(@NotNull Codec<E> elementCodec, boolean allowDuplicates) {
        Objects.requireNonNull(elementCodec);
        return Codec.codec(ListEncoder.create(elementCodec), SetDecoder.create(elementCodec, allowDuplicates)).named("SetCodec[" + elementCodec + "]");
    }

    private SetCodec() {
        throw new UnsupportedOperationException();
    }
}
