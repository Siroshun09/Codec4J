package dev.siroshun.codec4j.io.gson;

import com.google.gson.stream.JsonWriter;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.io.IOException;
import java.util.function.Function;

@NotNullByDefault
public class JsonWriterOut implements Out<Void>, AutoCloseable {

    private final JsonWriter writer;

    JsonWriterOut(JsonWriter writer) {
        this.writer = writer;
    }

    @Override
    public Result<Void, EncodeError> writeBoolean(boolean value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeByte(byte value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeChar(char value) {
        try {
            this.writer.value(String.valueOf(value));
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeDouble(double value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeFloat(float value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeInt(int value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeLong(long value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeShort(short value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Void, EncodeError> writeString(String value) {
        try {
            this.writer.value(value);
            return Result.success();
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<ElementAppender<Void>, EncodeError> createList() {
        try {
            this.writer.beginArray();
            return Result.success(new JsonElementAppender());
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<EntryAppender<Void>, EncodeError> createMap() {
        try {
            this.writer.beginObject();
            return Result.success(new JsonEntryAppender());
        } catch (IOException e) {
            return EncodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    private class JsonElementAppender implements ElementAppender<Void> {

        @Override
        public Result<Void, EncodeError> append(Function<Out<Void>, Result<Void, EncodeError>> function) {
            return function.apply(JsonWriterOut.this);
        }

        @Override
        public Result<Void, EncodeError> finish() {
            try {
                JsonWriterOut.this.writer.endArray();
                return Result.success();
            } catch (IOException e) {
                return EncodeError.fatalError(e).asFailure();
            }
        }
    }

    private class JsonEntryAppender implements EntryAppender<Void> {
        @Override
        public Result<Void, EncodeError> append(Function<Out<Void>, Result<Void, EncodeError>> keyWriter, Function<Out<Void>, Result<Void, EncodeError>> valueWriter) {
            var keyResult = keyWriter.apply(new JsonKeyOut());

            if (keyResult.isSuccess()) {
                return valueWriter.apply(JsonWriterOut.this);
            } else {
                return keyResult;
            }
        }

        @Override
        public Result<Void, EncodeError> finish() {
            try {
                JsonWriterOut.this.writer.endObject();
                return Result.success();
            } catch (IOException e) {
                return EncodeError.fatalError(e).asFailure();
            }
        }
    }

    private class JsonKeyOut implements Out<Void> {

        @Override
        public Result<Void, EncodeError> writeBoolean(boolean value) {
            return EncodeError.notWritableType(Type.BOOLEAN).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeByte(byte value) {
            return EncodeError.notWritableType(Type.BYTE).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeChar(char value) {
            try {
                JsonWriterOut.this.writer.name(String.valueOf(value));
                return Result.success();
            } catch (IOException e) {
                return EncodeError.fatalError(e).asFailure();
            }
        }

        @Override
        public Result<Void, EncodeError> writeDouble(double value) {
            return EncodeError.notWritableType(Type.DOUBLE).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeFloat(float value) {
            return EncodeError.notWritableType(Type.FLOAT).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeInt(int value) {
            return EncodeError.notWritableType(Type.INT).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeLong(long value) {
            return EncodeError.notWritableType(Type.LONG).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeShort(short value) {
            return EncodeError.notWritableType(Type.SHORT).asFailure();
        }

        @Override
        public Result<Void, EncodeError> writeString(String value) {
            try {
                JsonWriterOut.this.writer.name(value);
                return Result.success();
            } catch (IOException e) {
                return EncodeError.fatalError(e).asFailure();
            }
        }

        @Override
        public Result<ElementAppender<Void>, EncodeError> createList() {
            return EncodeError.notWritableType(Type.LIST).asFailure();
        }

        @Override
        public Result<EntryAppender<Void>, EncodeError> createMap() {
            return EncodeError.notWritableType(Type.MAP).asFailure();
        }
    }
}
