package dev.siroshun.codec4j.io.gzip;

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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@NotNullByDefault
public final class GzipIO implements FileIO {

    @Contract("_ -> new")
    public static GzipIO noCompression(FileIO io) {
        return create(io, Deflater.NO_COMPRESSION);
    }

    @Contract("_ -> new")
    public static GzipIO defaultCompression(FileIO io) {
        return create(io, Deflater.DEFAULT_COMPRESSION);
    }

    @Contract("_ -> new")
    public static GzipIO bestCompression(FileIO io) {
        return create(io, Deflater.BEST_COMPRESSION);
    }

    @Contract("_, _ -> new")
    public static GzipIO create(FileIO io, int compressionLevel) {
        Objects.requireNonNull(io);
        if (compressionLevel < Deflater.NO_COMPRESSION || compressionLevel > Deflater.BEST_COMPRESSION) {
            throw new IllegalArgumentException("compressionLevel must be between " + Deflater.NO_COMPRESSION + " and " + Deflater.BEST_COMPRESSION);
        }
        return new GzipIO(io, compressionLevel);
    }

    private final FileIO delegate;
    private final int compressionLevel;

    private GzipIO(FileIO delegate, int compressionLevel) {
        this.delegate = delegate;
        this.compressionLevel = compressionLevel;
    }

    @Override
    public @NotNull <T> Result<T, DecodeError> decodeFrom(@NotNull InputStream in, @NotNull Decoder<? extends T> decoder) {
        try (GZIPInputStream gzipIn = new GZIPInputStream(in)) {
            return this.delegate.decodeFrom(gzipIn, decoder);
        } catch (Exception e) {
            return Result.failure(DecodeError.fatalError(e));
        }
    }

    @Override
    public @NotNull <T> Result<Void, EncodeError> encodeTo(@NotNull OutputStream out, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(out) {
            {
                this.def.setLevel(GzipIO.this.compressionLevel);
            }
        }) {
            return this.delegate.encodeTo(gzipOut, encoder, input);
        } catch (Exception e) {
            return Result.failure(EncodeError.fatalError(e));
        }
    }
}
