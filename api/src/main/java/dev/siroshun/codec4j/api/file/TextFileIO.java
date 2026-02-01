package dev.siroshun.codec4j.api.file;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public interface TextFileIO extends FileIO {
    
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    <T> @NotNull Result<T, DecodeError> decodeFrom(@NotNull Reader reader, @NotNull Decoder<? extends T> decoder);

    <T> @NotNull Result<Void, EncodeError> encodeTo(@NotNull Writer writer, @NotNull Encoder<? super T> encoder, @UnknownNullability T input);

    @Override
    default <T> @NotNull Result<T, DecodeError> decodeFrom(@NotNull Path filepath, @NotNull Decoder<? extends T> decoder) {
        Objects.requireNonNull(filepath);
        Objects.requireNonNull(decoder);
        try (Reader reader = Files.isRegularFile(filepath) ? Files.newBufferedReader(filepath, DEFAULT_CHARSET) : Reader.nullReader()) {
            return this.decodeFrom(reader, decoder);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    default <T> @NotNull Result<Void, EncodeError> encodeTo(@NotNull Path filepath, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Objects.requireNonNull(filepath);
        Objects.requireNonNull(encoder);

        Result<Void, EncodeError> result = this.createParentDirectory(filepath);
        if (result.isFailure()) {
            return result.asFailure();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filepath, DEFAULT_CHARSET, DefaultOpenOptions.WRITE_OPEN_OPTIONS)) {
            return this.encodeTo(writer, encoder, input);
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    default <T> @NotNull Result<T, DecodeError> decodeFrom(@NotNull InputStream input, @NotNull Decoder<? extends T> decoder) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(decoder);
        try (InputStreamReader reader = new InputStreamReader(input, DEFAULT_CHARSET)) {
            return this.decodeFrom(reader, decoder);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    default <T> @NotNull Result<Void, EncodeError> encodeTo(@NotNull OutputStream output, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Objects.requireNonNull(output);
        Objects.requireNonNull(encoder);
        try (OutputStreamWriter writer = new OutputStreamWriter(output, DEFAULT_CHARSET)) {
            return this.encodeTo(writer, encoder, input);
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    default <T> @NotNull Result<T, DecodeError> decodeString(@NotNull Decoder<? extends T> decoder, @NotNull String str) {
        Objects.requireNonNull(decoder);
        Objects.requireNonNull(str);
        try (StringReader reader = new StringReader(str)) {
            return this.decodeFrom(reader, decoder);
        }
    }

    default <T> @NotNull Result<String, EncodeError> encodeToString(@NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Objects.requireNonNull(encoder);
        try (StringWriter writer = new StringWriter()) {
            Result<Void, EncodeError> result = this.encodeTo(writer, encoder, input);
            if (result.isFailure()) {
                return result.asFailure();
            }
            return Result.success(writer.toString());
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }
}
