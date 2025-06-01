package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.decoder.object.FieldDecoder;
import dev.siroshun.codec4j.api.encoder.object.FieldEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

record FieldCodecImpl<T, F>(@NotNull String fieldName, @NotNull FieldEncoder<T> encoder,
                            @NotNull FieldDecoder<F> decoder) implements FieldCodec<T, F> {
    @Override
    public @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in) {
        return this.decoder.decodeFieldValue(in);
    }

    @Override
    public @NotNull Result<F, DecodeError> onNotDecoded() {
        return this.decoder.onNotDecoded();
    }

    @Override
    public @NotNull <O> Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input) {
        return this.encoder.encodeFieldValue(out, input);
    }

    @Override
    public boolean canOmit(@UnknownNullability T input) {
        return this.encoder.canOmit(input);
    }
}
