package dev.siroshun.codec4j.api.file;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public interface FileIO {

    int DEFAULT_BUFFER_SIZE = 8192;

    @Contract(value = " -> new", pure = true)
    static @NotNull OpenOption @NotNull [] readOpenOptions() {
        return Arrays.copyOf(DefaultOpenOptions.READ_OPEN_OPTIONS, DefaultOpenOptions.READ_OPEN_OPTIONS.length);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull OpenOption @NotNull [] writeOpenOptions() {
        return Arrays.copyOf(DefaultOpenOptions.WRITE_OPEN_OPTIONS, DefaultOpenOptions.WRITE_OPEN_OPTIONS.length);
    }

    <T> @NotNull Result<T, DecodeError> decodeFrom(@NotNull InputStream in, @NotNull Decoder<? extends T> decoder);

    default <T> @NotNull Result<T, DecodeError> decodeFrom(@NotNull Path filepath, @NotNull Decoder<? extends T> decoder) {
        Objects.requireNonNull(filepath);
        Objects.requireNonNull(decoder);
        try (InputStream in = Files.isRegularFile(filepath) ? Files.newInputStream(filepath, DefaultOpenOptions.READ_OPEN_OPTIONS) : InputStream.nullInputStream();
             InputStream buffered = new BufferedInputStream(in, DEFAULT_BUFFER_SIZE)) {
            return this.decodeFrom(buffered, decoder);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    <T> @NotNull Result<Void, EncodeError> encodeTo(@NotNull OutputStream out, @NotNull Encoder<? super T> encoder, @UnknownNullability T input);

    default <T> @NotNull Result<Void, EncodeError> encodeTo(@NotNull Path filepath, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Objects.requireNonNull(filepath);
        Objects.requireNonNull(encoder);

        Result<Void, EncodeError> result = this.createParentDirectory(filepath);
        if (result.isFailure()) {
            return result.asFailure();
        }

        try (OutputStream out = Files.newOutputStream(filepath, DefaultOpenOptions.WRITE_OPEN_OPTIONS);
             BufferedOutputStream buffered = new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE)) {
            return this.encodeTo(buffered, encoder, input);
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    default <T> @NotNull Result<T, DecodeError> decodeBytes(@NotNull Decoder<? extends T> decoder, byte @NotNull [] bytes) {
        Objects.requireNonNull(decoder);
        Objects.requireNonNull(bytes);
        try (InputStream input = new ByteArrayInputStream(bytes)) {
            return this.decodeFrom(input, decoder);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    default <T> @NotNull Result<byte @NotNull [], EncodeError> encodeToBytes(@NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Objects.requireNonNull(encoder);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Result<Void, EncodeError> result = this.encodeTo(output, encoder, input);
            if (result.isFailure()) {
                return result.asFailure();
            }
            return Result.success(output.toByteArray());
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    default @NotNull Result<Void, EncodeError> createParentDirectory(@NotNull Path filepath) {
        Objects.requireNonNull(filepath);

        Path parent = filepath.getParent();

        if (parent != null && !Files.isDirectory(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                return EncodeError.fatalError(e).asFailure();
            }
        }

        return Result.success();
    }

}
