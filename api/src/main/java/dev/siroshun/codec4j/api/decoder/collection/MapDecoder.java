package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class MapDecoder {

    /**
     * Creates a {@link EntryDecoder} for decoding entries as a {@link Map}.
     * <p>
     * This decoder does not allow duplicate keys.
     *
     * @param keyDecoder   the {@link Decoder} for decoding keys
     * @param valueDecoder the {@link Decoder} for decoding values
     * @param <K>          the type of the key
     * @param <V>          the type of the value
     * @return a {@link EntryDecoder} for decoding entries as a {@link Map}
     */
    public static <K, V> @NotNull Decoder<Map<K, V>> create(@NotNull Decoder<K> keyDecoder, @NotNull Decoder<V> valueDecoder) {
        Objects.requireNonNull(keyDecoder);
        Objects.requireNonNull(valueDecoder);
        return create(keyDecoder, valueDecoder, HashMap::new, false, UnaryOperator.identity());
    }

    /**
     * Creates a {@link EntryDecoder} for decoding entries as a {@link Map}.
     *
     * @param keyDecoder         the {@link Decoder} for decoding keys
     * @param valueDecoder       the {@link Decoder} for decoding values
     * @param factory            the factory for creating a {@link Map}
     * @param allowDuplicatedKey whether to allow duplicate keys
     * @param finalizer          the finalizer for the {@link Map}
     * @param <K>                the type of the key
     * @param <V>                the type of the value
     * @return a {@link EntryDecoder} for decoding entries as a {@link Map}
     */
    public static <K, V> @NotNull Decoder<Map<K, V>> create(@NotNull Decoder<K> keyDecoder, @NotNull Decoder<V> valueDecoder,
                                                         @NotNull Supplier<Map<K, V>> factory, boolean allowDuplicatedKey,
                                                         @NotNull UnaryOperator<Map<K, V>> finalizer) {
        Objects.requireNonNull(keyDecoder);
        Objects.requireNonNull(valueDecoder);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(finalizer);
        MapEntryDecodeProcessor<K, V> processor = new MapEntryDecodeProcessor<>(keyDecoder, valueDecoder, factory, allowDuplicatedKey, finalizer);
        return (EntryDecoder<K, V, Map<K, V>>) () -> processor;
    }

    private MapDecoder() {
        throw new UnsupportedOperationException();
    }
}
