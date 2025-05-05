package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

/**
 * A {@link Codec} for {@link UUID}.
 */
@NotNullByDefault
public final class UUIDCodec {

    /**
     * A {@link Codec} for {@link UUID}.
     * <p>
     * This {@link Codec} encodes {@link UUID}s as {@link String} using {@link Out#writeString(String)}.
     * <p>
     * If the input string is not a valid {@link UUID}, this {@link Codec} will return a {@link InvalidUUIDFormatError} as a {@link DecodeError}.
     */
    public static final Codec<UUID> UUID_AS_STRING = Codec.STRING.flatXmap(
        uuid -> Result.success(uuid.toString()),
        str -> {
            try {
                return Result.success(UUID.fromString(str));
            } catch (IllegalArgumentException e) {
                return new InvalidUUIDFormatError(str).asFailure();
            }
        }
    );

    /**
     * A {@link DecodeError.Failure} for {@link UUIDCodec#UUID_AS_STRING}.
     *
     * @param input the input string that is not a valid {@link UUID}
     */
    public record InvalidUUIDFormatError(String input) implements DecodeError.Failure {
    }

    private UUIDCodec() {
        throw new UnsupportedOperationException();
    }
}
