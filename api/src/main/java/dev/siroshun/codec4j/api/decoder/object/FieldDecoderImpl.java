package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

record FieldDecoderImpl<F>(@NotNull String fieldName, @NotNull Decoder<F> decoder,
                           @NotNull Supplier<Result<F, DecodeError>> onNotDecodedSupplier) implements FieldDecoder<F> {
    @Override
    public @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in) {
        return this.decoder.decode(in);
    }

    @Override
    public @NotNull Result<F, DecodeError> onNotDecoded() {
        return this.onNotDecodedSupplier.get();
    }
}
