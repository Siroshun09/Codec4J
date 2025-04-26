package dev.siroshun.codec4j.io;


import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

@NotNullByDefault
final class MemoryValue implements Memory.Delegated {

    private Type type = Type.UNKNOWN;
    private @Nullable Object value;

    @Override
    public Type type() {
        return this.type;
    }

    Result<Boolean, DecodeError> readAsBoolean() {
        return this.value instanceof Boolean val ? Result.success(val) : DecodeError.typeMismatch(Type.BOOLEAN, this.type()).asFailure();
    }

    Result<Byte, DecodeError> readAsByte() {
        return this.value instanceof Byte val ? Result.success(val) : DecodeError.typeMismatch(Type.BYTE, this.type()).asFailure();
    }

    Result<Character, DecodeError> readAsChar() {
        return this.value instanceof Character val ? Result.success(val) : DecodeError.typeMismatch(Type.CHAR, this.type()).asFailure();
    }

    Result<Double, DecodeError> readAsDouble() {
        return this.value instanceof Double val ? Result.success(val) : DecodeError.typeMismatch(Type.DOUBLE, this.type()).asFailure();
    }

    Result<Float, DecodeError> readAsFloat() {
        return this.value instanceof Float val ? Result.success(val) : DecodeError.typeMismatch(Type.FLOAT, this.type()).asFailure();
    }

    Result<Integer, DecodeError> readAsInt() {
        return this.value instanceof Integer val ? Result.success(val) : DecodeError.typeMismatch(Type.INT, this.type()).asFailure();
    }

    Result<Long, DecodeError> readAsLong() {
        return this.value instanceof Long val ? Result.success(val) : DecodeError.typeMismatch(Type.LONG, this.type()).asFailure();
    }

    Result<Short, DecodeError> readAsShort() {
        return this.value instanceof Short val ? Result.success(val) : DecodeError.typeMismatch(Type.SHORT, this.type()).asFailure();
    }

    Result<String, DecodeError> readAsString() {
        return this.value instanceof String val ?
            Result.success(val) :
            this.type == Type.STRING ?
                Result.success() :
                DecodeError.typeMismatch(Type.STRING, this.type()).asFailure();
    }

    void writeBoolean(boolean value) {
        this.type = Type.BOOLEAN;
        this.value = value;
    }

    void writeByte(byte value) {
        this.type = Type.BYTE;
        this.value = value;
    }

    void writeChar(char value) {
        this.type = Type.CHAR;
        this.value = value;
    }

    void writeDouble(double value) {
        this.type = Type.DOUBLE;
        this.value = value;
    }

    void writeFloat(float value) {
        this.type = Type.FLOAT;
        this.value = value;
    }

    void writeInt(int value) {
        this.type = Type.INT;
        this.value = value;
    }

    void writeLong(long value) {
        this.type = Type.LONG;
        this.value = value;
    }

    void writeShort(short value) {
        this.type = Type.SHORT;
        this.value = value;
    }

    void writeString(String value) {
        this.type = Type.STRING;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value != null ?
            this.value.toString() :
            (this.type == Type.STRING ? "null" : "<NOT_SET>");
    }
}
