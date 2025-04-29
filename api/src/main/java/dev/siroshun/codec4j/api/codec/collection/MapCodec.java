package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * A {@link Codec} for {@link Map}.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public final class MapCodec<K, V> implements Codec<Map<K, V>> {

    /**
     * Creates a {@link MapCodec} with the specified key and value {@link Codec}.
     *
     * @param keyCodec   the {@link Codec} for keys
     * @param valueCodec the {@link Codec} for values
     * @param <K>        the type of the key
     * @param <V>        the type of the value
     * @return a {@link MapCodec}
     */
    @Contract("_, _ -> new")
    public static <K, V> @NotNull MapCodec<K, V> map(@NotNull Codec<K> keyCodec, @NotNull Codec<V> valueCodec) {
        return map(keyCodec, valueCodec, HashMap::new, false, UnaryOperator.identity());
    }

    /**
     * Creates a {@link MapCodec} with the specified key and value {@link Codec}, the custom {@link Map} factory, and other options.
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
    public static <K, V> @NotNull MapCodec<K, V> map(@NotNull Codec<K> keyCodec, @NotNull Codec<V> valueCodec,
                                                     @NotNull Supplier<Map<K, V>> factory, boolean allowDuplicatedKey,
                                                     @NotNull UnaryOperator<Map<K, V>> finalizer) {
        Objects.requireNonNull(keyCodec);
        Objects.requireNonNull(valueCodec);
        Objects.requireNonNull(factory);
        Objects.requireNonNull(finalizer);
        return new MapCodec<>(new Processor<>(keyCodec, valueCodec, factory, allowDuplicatedKey, finalizer));
    }

    /**
     * Creates a {@link MapCodec} with the specified {@link EntryProcessor}.
     *
     * @param processor the {@link EntryProcessor}
     * @param <K>       the type of the key
     * @param <V>       the type of the value
     * @return a {@link MapCodec}
     */
    @Contract(value = "_ -> new")
    public static <K, V> @NotNull MapCodec<K, V> create(@NotNull EntryProcessor<K, V, Map.Entry<K, V>, Map<K, V>> processor) {
        return new MapCodec<>(Objects.requireNonNull(processor));
    }

    private final Codec<Map<K, V>> codec;

    private MapCodec(@NotNull EntryProcessor<K, V, Map.Entry<K, V>, Map<K, V>> processor) {
        EntryEncoder<Map.Entry<K, V>, Map<K, V>> encoder = () -> processor;
        EntryDecoder<K, V, Map<K, V>> decoder = () -> processor;
        this.codec = Codec.codec(encoder, decoder);
    }

    @Override
    public @NotNull Result<Map<K, V>, DecodeError> decode(@NotNull In in) {
        return this.codec.decode(in);
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability Map<K, V> input) {
        return this.codec.encode(out, input);
    }

    /**
     * A {@link DecodeError} when an entry is duplicated during decoding into a map that does not allow duplicates.
     *
     * @param key  the duplicated key
     * @param value the value of the duplicated key
     */
    public record DuplicatedKeyError(@UnknownNullability Object key,
                                     @UnknownNullability Object value) implements DecodeError.Failure {
    }

    private record Processor<K, V>(@NotNull Codec<K> keyCodec, @NotNull Codec<V> valueCodec,
                                   @NotNull Supplier<Map<K, V>> factory, boolean allowDuplicatedKey,
                                   @NotNull UnaryOperator<Map<K, V>> finalizer) implements EntryProcessor<K, V, Map.Entry<K, V>, Map<K, V>> {

        @Override
        public @NotNull <O> Result<O, EncodeError> encodeKey(@NotNull Out<O> out, Map.@UnknownNullability Entry<K, V> entry) {
            return this.keyCodec.encode(out, Objects.requireNonNull(entry).getKey());
        }

        @Override
        public @NotNull <O> Result<O, EncodeError> encodeValue(@NotNull Out<O> out, Map.@UnknownNullability Entry<K, V> entry) {
            return this.valueCodec.encode(out, Objects.requireNonNull(entry).getValue());
        }

        @Override
        public @NotNull Iterator<Map.Entry<K, V>> toEntryIterator(@UnknownNullability Map<K, V> input) {
            return Objects.requireNonNull(input).entrySet().iterator();
        }

        @Override
        public @NotNull Result<K, DecodeError> decodeKey(@NotNull In in) {
            return this.keyCodec.decode(in);
        }

        @Override
        public @NotNull Result<V, DecodeError> decodeValue(@NotNull In in) {
            return this.valueCodec.decode(in);
        }

        @Override
        public @NotNull Map<K, V> createIdentity() {
            return this.factory.get();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptEntry(@NotNull Map<K, V> identity, @UnknownNullability K key, @UnknownNullability V value) {
            if (identity.putIfAbsent(key, value) != null && !this.allowDuplicatedKey) {
                return new DuplicatedKeyError(key, value).asFailure();
            }
            return Result.success();
        }

        @Override
        public @NotNull Map<K, V> finalizeIdentity(@NotNull Map<K, V> identity) {
            return this.finalizer.apply(identity);
        }
    }
}
