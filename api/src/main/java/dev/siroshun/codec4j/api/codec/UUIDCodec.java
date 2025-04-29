package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public final class UUIDCodec {

    /**
     * A {@link Codec} for {@link UUID}.
     * <p>
     * This {@link Codec} encodes {@link UUID}s as {@link String} using {@link Out#writeString(String)}.
     * <p>
     * If the input string is not a valid {@link UUID}, this {@link Codec} will return a {@link InvalidUUIDFormatError} as a {@link DecodeError}.
     */
    public static final Codec<UUID> UUID_AS_STRING = Codec.codec(UUIDCodec::encodeAsString, UUIDCodec::decodeFromString);

    private static <O> Result<O, EncodeError> encodeAsString(Out<O> out, UUID input) {
        return out.writeString(input.toString());
    }

    private static Result<UUID, DecodeError> decodeFromString(In in) {
        Result<String, DecodeError> result = in.readAsString();
        if (result.isFailure()) {
            return result.asFailure();
        }

        try {
            return Result.success(UUID.fromString(result.unwrap()));
        } catch (IllegalArgumentException e) {
            return new InvalidUUIDFormatError(result.unwrap()).asFailure();
        }
    }

    public record InvalidUUIDFormatError(String input) implements DecodeError.Failure {
    }

    private UUIDCodec() {
        throw new UnsupportedOperationException();
    }
}
