package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
        return create(new Processor<>(
                elementCodec,
                ArrayList::new,
                (list, element) -> {
                    list.add(element);
                    return Result.success();
                },
                UnaryOperator.identity()
        ));
    }

    /**
     * Creates a {@link CollectionCodec} for {@link Set} with the specified element type {@link E}.
     *
     * @param elementCodec the {@link Codec} for elements
     * @param <E>          the type of the element
     * @return a {@link CollectionCodec}
     */
    public static <E> @NotNull CollectionCodec<E, Set<E>> set(@NotNull Codec<E> elementCodec) {
        return create(new Processor<>(
                elementCodec,
                HashSet::new,
                (set, element) -> {
                    if (!set.add(element)) {
                        return new DuplicatedElementError(element).asFailure();
                    }
                    return Result.success();
                },
                UnaryOperator.identity()
        ));
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

    private record Processor<E, C extends Collection<E>>(@NotNull Codec<E> elementCodec, @NotNull Supplier<C> factory,
                                                         @NotNull BiFunction<C, E, Result<Void, DecodeError>> acceptor,
                                                         @NotNull UnaryOperator<C> finalizer) implements ElementProcessor<E, C> {

        @Override
        public @NotNull <O> Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element) {
            return this.elementCodec.encode(out, element);
        }

        @Override
        public @NotNull Iterator<E> toIterator(@UnknownNullability C input) {
            return Objects.requireNonNull(input).iterator();
        }

        @Override
        public @NotNull Result<E, DecodeError> decodeElement(@NotNull In in) {
            return this.elementCodec.decode(in);
        }

        @Override
        public @NotNull C createIdentity() {
            return this.factory.get();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptElement(@NotNull C identity, @UnknownNullability E element) {
            return this.acceptor.apply(identity, element);
        }

        @Override
        public @NotNull C finalizeIdentity(@NotNull C identity) {
            return this.finalizer.apply(identity);
        }
    }
}
