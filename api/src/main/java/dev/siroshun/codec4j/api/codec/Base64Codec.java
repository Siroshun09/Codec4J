package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Base64;

/**
 * A {@link Codec} for byte[].
 */
@NotNullByDefault
public final class Base64Codec {

    /**
     * A {@link Codec} for byte[] encoding as {@link String} using {@link Base64#getEncoder()} and decoding as byte[] using {@link Base64#getDecoder()}.
     */
    public static final Codec<byte[]> CODEC = Codec.STRING.flatXmap(
        bytes -> Result.success(Base64.getEncoder().encodeToString(bytes)),
        base64 -> {
            try {
                return Result.success(Base64.getDecoder().decode(base64));
            } catch (IllegalArgumentException e) {
                return new InvalidBase64Error(base64).asFailure();
            }
        }
    ).named("Base64Codec");

    /**
     * A {@link DecodeError.Failure} for {@link Base64Codec#CODEC}.
     *
     * @param input the input string that is not a valid Base64 string
     */
    public record InvalidBase64Error(String input) implements DecodeError.Failure {
    }

    private Base64Codec() {
        throw new UnsupportedOperationException();
    }
}
