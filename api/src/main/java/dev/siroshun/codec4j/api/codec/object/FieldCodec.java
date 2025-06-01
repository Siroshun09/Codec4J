package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.object.FieldDecoder;
import dev.siroshun.codec4j.api.encoder.object.FieldEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface FieldCodec<T, F> extends FieldEncoder<T>, FieldDecoder<F> {

    @Contract("_, _ -> new")
    static <F> @NotNull FieldCodecBuilder<F> builder(@NotNull String fieldName, @NotNull Codec<F> codec) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(codec);
        return new FieldCodecBuilder<>(fieldName, codec);
    }

    static <T, F> @NotNull FieldCodec<T, F> create(@NotNull FieldEncoder<T> encoder, @NotNull FieldDecoder<F> decoder) {
        Objects.requireNonNull(encoder);
        Objects.requireNonNull(decoder);
        if (!encoder.fieldName().equals(decoder.fieldName())) {
            throw new IllegalArgumentException("Field name of encoder and decoder must be same.");
        }
        return new FieldCodecImpl<>(encoder.fieldName(), encoder, decoder);
    }

}
