package dev.siroshun.codec4j.io.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.file.TextFileIO;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.UnknownNullability;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * An implementation of {@link TextFileIO} for Gson.
 */
@NotNullByDefault
public final class GsonIO implements TextFileIO {

    /**
     * The default {@link GsonIO}.
     */
    public static final GsonIO DEFAULT = create(new Gson());

    /**
     * The pretty-printing {@link GsonIO}.
     */
    public static final GsonIO PRETTY_PRINTING = create(new GsonBuilder().setPrettyPrinting().create());

    /**
     * Creates a new {@link GsonIO} from the custom {@link Gson}.
     *
     * @param gson the {@link Gson} to create {@link GsonIO} from
     * @return the {@link GsonIO}
     */
    @Contract(value = "_ -> new", pure = true)
    public static GsonIO create(Gson gson) {
        return new GsonIO(gson);
    }

    private final Gson gson;

    private GsonIO(Gson gson) {
        this.gson = gson;
    }

    /**
     * Creates a new {@link JsonReaderIn} from {@link Reader}.
     *
     * @param reader the {@link Reader} to create {@link JsonReaderIn} from
     * @return the {@link JsonReaderIn}
     */
    public JsonReaderIn newIn(Reader reader) {
        return new JsonReaderIn(this.gson.newJsonReader(reader));
    }

    /**
     * Creates a new {@link JsonWriterOut} from {@link Writer}.
     *
     * @param writer the {@link Writer} to create {@link JsonWriterOut} from
     * @return the {@link JsonWriterOut}
     * @throws IOException if an I/O error occurs
     */
    public JsonWriterOut newOut(Writer writer) throws IOException {
        return new JsonWriterOut(this.gson.newJsonWriter(writer));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Result<T, DecodeError> decodeFrom(Reader reader, Decoder<? extends T> decoder) {
        Result<T, DecodeError> result = null;
        try (JsonReaderIn in = this.newIn(reader)) {
            result = (Result<T, DecodeError>) decoder.decode(in);
        } catch (IOException e) {
            return result.isFailure() ?
                DecodeError.multipleError(List.of(result.unwrapError(), DecodeError.fatalError(e))).asFailure() :
                DecodeError.fatalError(e).asFailure();
        }
        return result;
    }

    @Override
    public @NotNull <T> Result<Void, EncodeError> encodeTo(@NotNull Writer writer, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        Result<Void, EncodeError> result = null;
        try (JsonWriterOut out = this.newOut(writer)) {
            result = encoder.encode(out, input);
        } catch (IOException e) {
            return result != null && result.isFailure() ?
                EncodeError.multipleError(List.of(result.unwrapError(), EncodeError.fatalError(e))).asFailure() :
                EncodeError.fatalError(e).asFailure();
        }
        return result;
    }
}
