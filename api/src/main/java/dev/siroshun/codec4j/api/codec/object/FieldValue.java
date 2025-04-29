package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class FieldValue<F> {

    private final FieldCodec<?, F> codec;
    private @Nullable F value;
    private boolean decoded;

    FieldValue(@NotNull FieldCodec<?, F> codec) {
        this.codec = codec;
    }

    @NotNull Result<Void, DecodeError> decode(@NotNull In in) {
        if (this.decoded) {
            return new FieldCodec.AlreadyDecodedError(this.codec.fieldName()).asFailure();
        }

        var result = this.codec.decodeFieldValue(in);
        this.decoded = true;

        if (result.isSuccess()) {
            this.value = result.unwrap();
            return Result.success();
        }

        if (!(result.unwrapError() instanceof DecodeError.IgnorableError)) {
            return result.asFailure();
        }

        var fallbackValueResult = this.codec.fallbackValue();

        if (fallbackValueResult.isSuccess()) {
            this.value = fallbackValueResult.unwrap();
            return Result.success();
        }

        return fallbackValueResult.asFailure();
    }

    @NotNull Result<Void, DecodeError> checkDecoded() {
        if (this.decoded) {
            return Result.success();
        }

        Result<F, DecodeError> result = this.codec.fallbackValue();

        if (result.isSuccess()) {
            this.value = result.unwrap();
            return Result.success();
        }

        return result.asFailure();
    }

    @Nullable F result() {
        if (!this.decoded) {
            throw new IllegalStateException("Not decoded.");
        }

        return this.value;
    }
}
