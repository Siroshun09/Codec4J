package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class ListDecoder {

    /**
     * Creates a {@link ElementDecoder} for decoding elements as a {@link List}.
     *
     * @param elementDecoder the {@link Decoder} for decoding elements
     * @param <E>            the type of the element
     * @return a {@link ElementDecoder} for decoding elements as a {@link List}
     */
    public static <E> @NotNull Decoder<List<E>> create(@NotNull Decoder<E> elementDecoder) {
        Objects.requireNonNull(elementDecoder);
        ElementDecoder.DecodeProcessor<E, List<E>> processor = new DecodeProcessorImpl<>(
            elementDecoder,
            ArrayList::new,
            (list, element) -> {
                list.add(element);
                return Result.success();
            },
            UnaryOperator.identity()
        );
        return (ElementDecoder<E, List<E>>) () -> processor;
    }

    private ListDecoder() {
        throw new UnsupportedOperationException();
    }
}
