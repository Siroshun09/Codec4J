package dev.siroshun.codec4j.api.encoder.collection;

import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;

record IterableEncodeProcessor<E, C extends Iterable<E>>(
    @NotNull Encoder<E> encoder) implements ElementEncoder.EncodeProcessor<E, C> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element) {
        return this.encoder.encode(out, element);
    }

    @Override
    public @NotNull Iterator<E> toIterator(@UnknownNullability C input) {
        return input.iterator();
    }

}
