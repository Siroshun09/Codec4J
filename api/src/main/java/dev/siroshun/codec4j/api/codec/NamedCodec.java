package dev.siroshun.codec4j.api.codec;

import org.jetbrains.annotations.NotNull;

record NamedCodec<T>(@NotNull String name, @NotNull Codec<T> codec) implements Codec.Delegated<T> {
    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public @NotNull Codec<T> named(@NotNull String name) {
        return new NamedCodec<>(name, this.codec);
    }
}
