package dev.siroshun.codec4j.api.encoder.collection;

import dev.siroshun.codec4j.api.encoder.Encoder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class MapEncoder {

    public static <K, V> @NotNull Encoder<Map<K, V>> create(@NotNull Encoder<K> keyEncoder, @NotNull Encoder<V> valueEncoder) {
        Objects.requireNonNull(keyEncoder);
        Objects.requireNonNull(valueEncoder);
        MapEntryEncodeProcessor<K, V> processor = new MapEntryEncodeProcessor<>(keyEncoder, valueEncoder);
        return (EntryEncoder<Map.Entry<K, V>, Map<K, V>>) () -> processor;
    }

    private MapEncoder() {
        throw new UnsupportedOperationException();
    }
}
