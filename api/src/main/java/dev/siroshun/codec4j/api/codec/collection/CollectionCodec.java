package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Set;

/**
 * A {@link Codec} for collections such as {@link List} and {@link Set}.
 *
 * @param <E> the type of the element
 * @param <C> the type of the collection
 */
public final class CollectionCodec<E, C> implements Codec<C> {

    /**
     * Creates a {@link CollectionCodec} for {@link List} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link CollectionCodec}
     */
    public static <E> @NotNull CollectionCodec<E, List<E>> list(@NotNull Codec<E> elementCodec) {
        return create(new ElementProcessors.ListProcessor<>(elementCodec));
    }

    /**
     * Creates a {@link CollectionCodec} for {@link Set} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link CollectionCodec}
     */
    public static <E> @NotNull CollectionCodec<E, Set<E>> set(@NotNull Codec<E> elementCodec) {
        return create(new ElementProcessors.SetProcessor<>(elementCodec));
    }

    /**
     * Creates a {@link CollectionCodec} with the {@link ElementProcessor}.
     *
     * @param processor the {@link ElementProcessor}
     * @param <E>       the type of the element
     * @param <T>       the type of the collection
     * @return a {@link CollectionCodec}
     */
    public static <E, T> @NotNull CollectionCodec<E, T> create(@NotNull ElementProcessor<E, T> processor) {
        return new CollectionCodec<>(processor);
    }

    private final Codec<C> codec;

    private CollectionCodec(@NotNull ElementProcessor<E, C> processor) {
        ElementEncoder<E, C> encoder = () -> processor;
        ElementDecoder<E, C> decoder = () -> processor;
        this.codec = Codec.codec(encoder, decoder);
    }

    @Override
    public @NotNull Result<C, DecodeError> decode(@NotNull In in) {
        return this.codec.decode(in);
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability C input) {
        return this.codec.encode(out, input);
    }

    /**
     * A {@link DecodeError} when an element is duplicated during decoding into a collection that does not allow duplicates.
     *
     * @param element the duplicated element
     */
    public record DuplicatedElementError(Object element) implements DecodeError.Failure {
    }
}
