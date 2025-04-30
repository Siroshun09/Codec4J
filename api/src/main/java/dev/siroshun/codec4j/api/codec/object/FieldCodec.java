package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;

public interface FieldCodec<T, F> {

    @Contract("_, _, _ -> new")
    static <T, F> @NotNull FieldCodecBuilder<T, F> builder(@NotNull String fieldName, @NotNull Codec<F> codec, @NotNull Function<T, F> getter) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(codec);
        Objects.requireNonNull(getter);
        return new FieldCodecBuilder<>(fieldName, codec, getter);
    }

    @NotNull String fieldName();

    <O> @NotNull Result<O, EncodeError> encodeFieldValue(@NotNull Out<O> out, @UnknownNullability T input);

    boolean canOmit(@UnknownNullability T input);

    @NotNull Result<F, DecodeError> decodeFieldValue(@NotNull In in);

    @NotNull Result<F, DecodeError> onNotDecoded();

    record RequiredFieldError(String fieldName) implements DecodeError.Failure {
    }

    record AlreadyDecodedError(String fieldName) implements DecodeError.Failure {
    }
}
