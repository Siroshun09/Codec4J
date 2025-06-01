package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class FieldValue<F> {

    private final FieldDecoder<F> codec;
    private @Nullable F value;
    private boolean decoded;

    FieldValue(@NotNull FieldDecoder<F> codec) {
        this.codec = codec;
    }

    @NotNull Result<Void, DecodeError> decode(@NotNull In in) {
        if (this.decoded) {
            return new AlreadyDecodedError(this.codec.fieldName()).asFailure();
        }

        Result<F, DecodeError> result = this.codec.decodeFieldValue(in);
        this.decoded = true;

        if (result.isSuccess()) {
            this.value = result.unwrap();
            return Result.success();
        }

        if (!(result.unwrapError() instanceof DecodeError.IgnorableError)) {
            return result.asFailure();
        }

        Result<F, DecodeError> onNotDecoded = this.codec.onNotDecoded();

        if (onNotDecoded.isSuccess()) {
            this.value = onNotDecoded.unwrap();
            return Result.success();
        }

        return onNotDecoded.asFailure();
    }

    @NotNull Result<Void, DecodeError> checkDecoded() {
        if (this.decoded) {
            return Result.success();
        }

        Result<F, DecodeError> result = this.codec.onNotDecoded();

        if (result.isSuccess()) {
            this.value = result.unwrap();
            this.decoded = true;
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
