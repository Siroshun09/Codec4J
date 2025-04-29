package dev.siroshun.codec4j.io.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;

import java.io.IOException;
import java.util.function.BiFunction;

@NotNullByDefault
public class JsonReaderIn implements In, AutoCloseable {

    private final JsonReader reader;

    JsonReaderIn(JsonReader reader) {
        this.reader = reader;
    }

    @Override
    public Result<Type, DecodeError> type() {
        try {
            return Result.success(convertType(this.reader.peek()));
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    private static Type convertType(JsonToken token) {
        return switch (token) {
            case BEGIN_ARRAY, END_ARRAY -> Type.LIST;
            case BEGIN_OBJECT, END_OBJECT -> Type.MAP;
            case NAME, STRING -> Type.STRING;
            case NUMBER -> Type.DOUBLE;
            case BOOLEAN -> Type.BOOLEAN;
            case NULL, END_DOCUMENT -> Type.UNKNOWN;
        };
    }

    @Override
    public Result<Boolean, DecodeError> readAsBoolean() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.BOOLEAN) {
                return Result.success(this.reader.nextBoolean());
            } else if (token == JsonToken.NULL) {
                return Result.success(false);
            } else {
                return DecodeError.typeMismatch(Type.BOOLEAN, convertType(token)).asFailure();
            }
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Byte, DecodeError> readAsByte() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                long value = this.reader.nextLong();
                return Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE ?
                    Result.success((byte) value) :
                    DecodeError.invalidNumber(Type.BYTE, value).asFailure();
            } else if (token == JsonToken.NULL) {
                return Result.success((byte) 0);
            } else {
                return DecodeError.typeMismatch(Type.BYTE, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Character, DecodeError> readAsChar() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.STRING) {
                var value = this.reader.nextString();
                return value.length() == 1 ?
                    Result.success(value.charAt(0)) :
                    DecodeError.invalidChar(value).asFailure();
            } else {
                return DecodeError.typeMismatch(Type.CHAR, convertType(token)).asFailure();
            }
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Double, DecodeError> readAsDouble() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                return Result.success(this.reader.nextDouble());
            } else if (token == JsonToken.NULL) {
                return Result.success(0.0);
            } else {
                return DecodeError.typeMismatch(Type.DOUBLE, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Float, DecodeError> readAsFloat() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                return Result.success((float) this.reader.nextDouble());
            } else if (token == JsonToken.NULL) {
                return Result.success(0f);
            } else {
                return DecodeError.typeMismatch(Type.FLOAT, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Integer, DecodeError> readAsInt() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                return Result.success(this.reader.nextInt());
            } else if (token == JsonToken.NULL) {
                return Result.success(0);
            } else {
                return DecodeError.typeMismatch(Type.INT, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Long, DecodeError> readAsLong() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                return Result.success(this.reader.nextLong());
            } else if (token == JsonToken.NULL) {
                return Result.success(0L);
            } else {
                return DecodeError.typeMismatch(Type.LONG, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<Short, DecodeError> readAsShort() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                long value = this.reader.nextLong();
                return Short.MIN_VALUE <= value && value <= Short.MAX_VALUE ?
                    Result.success((short) value) :
                    DecodeError.invalidNumber(Type.SHORT, value).asFailure();
            } else if (token == JsonToken.NULL) {
                return Result.success((short) 0);
            } else {
                return DecodeError.typeMismatch(Type.SHORT, convertType(token)).asFailure();
            }
        } catch (NumberFormatException e) {
            return DecodeError.invalidNumberFormat(e).asFailure();
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public Result<String, DecodeError> readAsString() {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.NUMBER || token == JsonToken.STRING) {
                return Result.success(this.reader.nextString());
            } else if (token == JsonToken.NULL) {
                return Result.success();
            } else {
                return DecodeError.typeMismatch(Type.STRING, convertType(token)).asFailure();
            }
        } catch (IOException | IllegalStateException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public @NotNull <R> Result<R, DecodeError> readList(@NotNull R identity, @NotNull BiFunction<R, ? super In, Result<?, ?>> operator) {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.BEGIN_ARRAY) {
                this.reader.beginArray();

                while (this.reader.hasNext()) {
                    var result = operator.apply(identity, this);
                    if (result.isFailure()) {
                        return DecodeError.iterationError(result.asFailure()).asFailure();
                    }
                }

                this.reader.endArray();
                return Result.success(identity);
            } else {
                return DecodeError.typeMismatch(Type.LIST, convertType(token)).asFailure();
            }
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public @NotNull <R> Result<R, DecodeError> readMap(@NotNull R identity, @NotNull BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
        try {
            var token = this.reader.peek();
            if (token == JsonToken.BEGIN_OBJECT) {
                this.reader.beginObject();

                while (this.reader.hasNext()) {
                    var result = operator.apply(identity, new JsonEntryIn());
                    if (result.isFailure()) {
                        return DecodeError.iterationError(result.asFailure()).asFailure();
                    }
                }

                this.reader.endObject();
                return Result.success(identity);
            } else {
                return DecodeError.typeMismatch(Type.MAP, convertType(token)).asFailure();
            }
        } catch (IOException e) {
            return DecodeError.fatalError(e).asFailure();
        }
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    private class JsonEntryIn implements EntryIn {

        private final JsonKeyIn keyIn = new JsonKeyIn();

        @Override
        public In keyIn() {
            return this.keyIn;
        }

        @Override
        public In valueIn() {
            if (!this.keyIn.read) {
                throw new IllegalStateException("The key should be read before reading the value.");
            }

            return JsonReaderIn.this;
        }
    }

    private class JsonKeyIn implements In {

        private boolean read;

        @Override
        public Result<Type, DecodeError> type() {
            return JsonReaderIn.this.type();
        }

        @Override
        public Result<Boolean, DecodeError> readAsBoolean() {
            return DecodeError.typeMismatch(Type.BOOLEAN, Type.STRING).asFailure();
        }

        @Override
        public Result<Byte, DecodeError> readAsByte() {
            return DecodeError.typeMismatch(Type.BYTE, Type.STRING).asFailure();
        }

        @Override
        public Result<Character, DecodeError> readAsChar() {
            return DecodeError.typeMismatch(Type.CHAR, Type.STRING).asFailure();
        }

        @Override
        public Result<Double, DecodeError> readAsDouble() {
            return DecodeError.typeMismatch(Type.DOUBLE, Type.STRING).asFailure();
        }

        @Override
        public Result<Float, DecodeError> readAsFloat() {
            return DecodeError.typeMismatch(Type.FLOAT, Type.STRING).asFailure();
        }

        @Override
        public Result<Integer, DecodeError> readAsInt() {
            return DecodeError.typeMismatch(Type.INT, Type.STRING).asFailure();
        }

        @Override
        public Result<Long, DecodeError> readAsLong() {
            return DecodeError.typeMismatch(Type.LONG, Type.STRING).asFailure();
        }

        @Override
        public Result<Short, DecodeError> readAsShort() {
            return DecodeError.typeMismatch(Type.SHORT, Type.STRING).asFailure();
        }

        @Override
        public Result<String, DecodeError> readAsString() {
            if (this.read) {
                throw new IllegalStateException("Cannot read the key twice.");
            }
            try {
                var name = JsonReaderIn.this.reader.nextName();
                this.read = true;
                return Result.success(name);
            } catch (IOException e) {
                return DecodeError.fatalError(e).asFailure();
            }
        }

        @Override
        public @NotNull <R> Result<R, DecodeError> readList(@NotNull R identity, @NotNull BiFunction<R, ? super In, Result<?, ?>> operator) {
            return DecodeError.typeMismatch(Type.LIST, Type.STRING).asFailure();
        }

        @Override
        public @NotNull <R> Result<R, DecodeError> readMap(@NotNull R identity, @NotNull BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
            return DecodeError.typeMismatch(Type.STRING, Type.STRING).asFailure();
        }
    }
}
