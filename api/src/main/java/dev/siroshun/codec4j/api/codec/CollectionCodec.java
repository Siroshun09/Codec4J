package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.decoder.element.ElementDecoder;
import dev.siroshun.codec4j.api.encoder.element.ElementEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Codec} for collections such as {@link List} and {@link Set}.
 */
public final class CollectionCodec {

    /**
     * Creates a {@link Codec} for {@link List} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link Codec}
     */
    public static <E> @NotNull Codec<List<E>> list(@NotNull Codec<E> elementCodec) {
        Objects.requireNonNull(elementCodec);
        return Codec.codec(ElementEncoder.create(elementCodec), ElementDecoder.list(elementCodec));
    }

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
        return Codec.codec(ElementEncoder.create(elementCodec), ElementDecoder.set(elementCodec));
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
        return Codec.codec(ElementEncoder.create(elementCodec), ElementDecoder.set(elementCodec, allowDuplicates));
    }

    /**
     * Creates a {@link CollectionCodec} for {@link Collection} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link CollectionCodec}
     */
    public static <E> @NotNull Codec<Collection<E>> collection(@NotNull Codec<E> elementCodec) {
        Objects.requireNonNull(elementCodec);
        return Codec.codec(ElementEncoder.create(elementCodec), ElementDecoder.list(elementCodec));
    }

    private CollectionCodec() {
        throw new UnsupportedOperationException();
    }
}
