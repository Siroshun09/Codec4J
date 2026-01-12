package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

public final class SetDecoder {

    /**
     * Creates a {@link ElementDecoder} for decoding elements as a {@link Set}.
     * <p>
     * This decoder does not allow duplicate elements.
     *
     * @param elementDecoder the {@link Decoder} for decoding elements
     * @param <E>            the type of the element
     * @return a {@link ElementDecoder} for decoding elements as a {@link Set}
     */
    public static <E> @NotNull Decoder<Set<E>> create(@NotNull Decoder<E> elementDecoder) {
        return create(elementDecoder, false);
    }

    /**
     * Creates a {@link ElementDecoder} for decoding elements as a {@link Set}.
     *
     * @param elementDecoder  the {@link Decoder} for decoding elements
     * @param allowDuplicates whether to allow duplicate elements
     * @param <E>             the type of the element
     * @return a {@link ElementDecoder} for decoding elements as a {@link Set}
     */
    public static <E> @NotNull Decoder<Set<E>> create(@NotNull Decoder<E> elementDecoder, boolean allowDuplicates) {
        ElementDecoder.DecodeProcessor<E, Set<E>> processor = new DecodeProcessorImpl<>(
            elementDecoder,
            HashSet::new,
            (set, element) -> {
                if (!set.add(element) && !allowDuplicates) {
                    return new DuplicatedElementError(element).asFailure();
                }
                return Result.success();
            },
            UnaryOperator.identity()
        );
        return (ElementDecoder<E, Set<E>>) () -> processor;
    }

    private SetDecoder() {
        throw new UnsupportedOperationException();
    }
}
