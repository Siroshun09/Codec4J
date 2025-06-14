package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.decoder.entry.EntryDecoder;
import dev.siroshun.codec4j.api.encoder.entry.EntryEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A {@link Codec} for {@link Map}.
 */
public final class MapCodec {

    /**
     * Creates a {@link Codec} for {@link Map}s with the specified key and value {@link Codec}.
     *
     * @param keyCodec   the {@link Codec} for keys
     * @param valueCodec the {@link Codec} for values
     * @param <K>        the type of the key
     * @param <V>        the type of the value
     * @return a {@link Codec} for {@link Map}s
     */
    @Contract("_, _ -> new")
    public static <K, V> @NotNull Codec<Map<K, V>> map(@NotNull Codec<K> keyCodec, @NotNull Codec<V> valueCodec) {
        return Codec.codec(
            EntryEncoder.map(keyCodec, valueCodec),
            EntryDecoder.map(keyCodec, valueCodec)
        ).named("MapCodec[key=" + keyCodec + ",value=" + valueCodec + "]");
    }

    /**
     * Creates a {@link Codec} for {@link Map}s with the specified key and value {@link Codec}, the custom {@link Map} factory, and other options.
     *
     * @param keyCodec           the {@link Codec} for keys
     * @param valueCodec         the {@link Codec} for values
     * @param factory            the custom {@link Map} factory
     * @param allowDuplicatedKey whether to allow duplicated keys
     * @param finalizer          the custom {@link Map} finalizer
     * @param <K>                the type of the key
     * @param <V>                the type of the value
     * @return a {@link MapCodec}
     */
    @Contract("_, _, _, _, _ -> new")
    public static <K, V> @NotNull Codec<Map<K, V>> map(@NotNull Codec<K> keyCodec, @NotNull Codec<V> valueCodec,
                                                       @NotNull Supplier<Map<K, V>> factory, boolean allowDuplicatedKey,
                                                       @NotNull UnaryOperator<Map<K, V>> finalizer) {
        return Codec.codec(
            EntryEncoder.map(keyCodec, valueCodec),
            EntryDecoder.map(keyCodec, valueCodec, factory, allowDuplicatedKey, finalizer)
        ).named("MapCodec[key=" + keyCodec + ",value=" + valueCodec + "]");
    }

    private MapCodec() {
        throw new UnsupportedOperationException();
    }
}
