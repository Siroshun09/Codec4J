package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record MapSource<K, V>(Supplier<Stream<Map.Entry<K, V>>> entryStreamSupplier,
                              Codec<Map<K, V>> codec) implements Source<Map<K, V>> {

    public static <V> MapSource<String, V> fromSourceWithStringKey(Source<V> source) {
        return new MapSource<>(
            () -> source.values().map(value -> Map.entry(String.valueOf(value), value)),
            source.codec().toMapCodecAsValue(Codec.STRING)
        );
    }

    @Override
    public Stream<Map<K, V>> values() {
        return Stream.concat(
            Stream.of(this.empty(), this.byMultipleEntries()),
            this.bySingleEntry()
        );
    }

    public Map<K, V> empty() {
        return Map.of();
    }

    public Stream<Map<K, V>> bySingleEntry() {
        return this.entryStreamSupplier.get().map(Map::ofEntries);
    }

    @SuppressWarnings("unchecked")
    public Map<K, V> byMultipleEntries() {
        return Map.ofEntries(this.entryStreamSupplier.get().toArray(Map.Entry[]::new));
    }
}
