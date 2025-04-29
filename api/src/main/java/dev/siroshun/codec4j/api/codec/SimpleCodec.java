package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Function;

record SimpleCodec<T>(@NotNull Encoder<? super T> encoder, @NotNull Decoder<? extends T> decoder) implements Codec<T> {

    @Override
    public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.encoder.encode(out, input);
    }

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return this.decoder.decode(in).map(Function.identity());
    }

}
