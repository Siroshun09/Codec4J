package dev.siroshun.codec4j.io.base64;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.UnknownNullability;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

@NotNullByDefault
public final class Base64IO implements FileIO {

    @Contract("_ -> new")
    public static Base64IO create(FileIO io) {
        return new Base64IO(io, Base64.getEncoder(), Base64.getDecoder());
    }

    @Contract("_ -> new")
    public static Base64IO createUrlBase64(FileIO io) {
        return new Base64IO(io, Base64.getUrlEncoder(), Base64.getUrlDecoder());
    }

    private final FileIO delegate;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    private Base64IO(FileIO delegate, Base64.Encoder encoder, Base64.Decoder decoder) {
        this.delegate = delegate;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public @NotNull <T> Result<T, DecodeError> decodeFrom(@NotNull InputStream in, @NotNull Decoder<? extends T> decoder) {
        try (InputStream base64In = this.decoder.wrap(in)) {
            return this.delegate.decodeFrom(base64In, decoder);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public @NotNull <T> Result<Void, EncodeError> encodeTo(@NotNull OutputStream out, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        try (OutputStream base64Out = this.encoder.wrap(out)) {
            return this.delegate.encodeTo(base64Out, encoder, input);
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }
}
