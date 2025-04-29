package dev.siroshun.codec4j.io.gson;

import com.google.gson.Gson;
import dev.siroshun.codec4j.api.codec.Decoder;
import dev.siroshun.codec4j.api.codec.Encoder;
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
        try (var in = this.newIn(reader)) {
            return (Result<T, DecodeError>) decoder.decode(in);
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public @NotNull <T> Result<Void, EncodeError> encodeTo(@NotNull Writer writer, @NotNull Encoder<? super T> encoder, @UnknownNullability T input) {
        try (var out = this.newOut(writer)) {
            return encoder.encode(out, input);
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }
}
