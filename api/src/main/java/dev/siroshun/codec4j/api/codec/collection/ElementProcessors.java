package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

final class ElementProcessors {

    private ElementProcessors() {
        throw new UnsupportedOperationException();
    }

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

    record DecodeProcessorImpl<E, C>(@NotNull Decoder<E> decoder,
                                     @NotNull Supplier<C> factory,
                                     @NotNull BiFunction<C, E, Result<Void, DecodeError>> acceptor,
                                     @NotNull UnaryOperator<C> finalizer) implements ElementDecoder.DecodeProcessor<E, C> {

        @Override
        public @NotNull Result<E, DecodeError> decodeElement(@NotNull In in) {
            return this.decoder.decode(in);
        }

        @Override
        public @NotNull C createIdentity() {
            return this.factory.get();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptElement(@NotNull C identity, @UnknownNullability E element) {
            return this.acceptor.apply(identity, element);
        }

        @Override
        public @NotNull C finalizeIdentity(@NotNull C identity) {
            return this.finalizer.apply(identity);
        }
    }
}
