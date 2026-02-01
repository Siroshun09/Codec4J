package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.collection.MapCodec;
import dev.siroshun.codec4j.api.encoder.collection.MapEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.EntryReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record MapSource<K, V>(Supplier<Stream<Map.Entry<K, V>>> entryStreamSupplier,
                              Codec<Map<K, V>> codec) implements Source<Map<K, V>> {

    public static <V> MapSource<String, V> fromSourceWithStringKey(Source<V> source) {
        return new MapSource<>(
            () -> source.values().map(value -> Map.entry(String.valueOf(value), value)),
            MapCodec.create(Codec.STRING, source.codec())
        );
    }

    public static <V> MapSource<String, V> fromSourceWithStringKeyForEntryReader(Source<V> source) {
        return new MapSource<>(
            () -> source.values().map(value -> Map.entry(String.valueOf(value), value)),
            new EntryReaderMapCodec<>(Codec.STRING, source.codec())
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

    private record EntryReaderMapCodec<K, V>(@NotNull Codec<K> keyCodec,
                                             @NotNull Codec<V> valueCodec) implements Codec<Map<K, V>> {

        @Override
        public @NotNull Result<Map<K, V>, DecodeError> decode(@NotNull In in) {
            Result<EntryReader, DecodeError> readerResult = in.readMap();
            if (readerResult.isFailure()) {
                return readerResult.asFailure();
            }

            EntryReader reader = readerResult.unwrap();
            Map<K, V> map = new LinkedHashMap<>();
            while (reader.hasNext()) {
                Result<EntryIn, DecodeError> entryIn = reader.next();
                if (entryIn.isFailure()) {
                    return entryIn.asFailure();
                }

                Result<K, DecodeError> keyResult = this.keyCodec.decode(entryIn.unwrap().keyIn());
                Result<V, DecodeError> valueResult = this.valueCodec.decode(entryIn.unwrap().valueIn());

                if (keyResult.isFailure()) {
                    return keyResult.asFailure();
                }

                if (valueResult.isFailure()) {
                    return valueResult.asFailure();
                }

                map.put(keyResult.unwrap(), valueResult.unwrap());
            }

            Result<Void, DecodeError> finishResult = reader.finish();
            if (finishResult.isFailure()) {
                return finishResult.asFailure();
            }

            return Result.success(map);
        }

        @Override
        public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, Map<K, V> input) {
            return MapEncoder.create(this.keyCodec, this.valueCodec).encode(out, input);
        }
    }
}
