package dev.siroshun.codec4j.api.decoder.element;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
