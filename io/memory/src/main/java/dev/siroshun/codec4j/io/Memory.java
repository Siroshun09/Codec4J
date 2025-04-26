package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A implementation of {@link In} that stores the data in memory.
 */
@NotNullByDefault
public final class Memory implements In {

    /**
     * Gets a {@link Out} for writing data to a {@link Memory}.
     *
     * @return a {@link Out} for writing data to a {@link Memory}
     */
    public static Out<Memory> out() {
        return MemoryWriter.INSTANCE;
    }

    private final Memory.Delegated delegate;

    Memory(Memory.Delegated delegate) {
        this.delegate = delegate;
    }

    @Override
    public Result<Type, DecodeError> type() {
        return Result.success(this.delegate.type());
    }

    @Override
    public Result<Boolean, DecodeError> readAsBoolean() {
        return this.readValue(Type.BOOLEAN, MemoryValue::readAsBoolean);
    }

    @Override
    public Result<Byte, DecodeError> readAsByte() {
        return this.readValue(Type.BYTE, MemoryValue::readAsByte);
    }

    @Override
    public Result<Character, DecodeError> readAsChar() {
        return this.readValue(Type.CHAR, MemoryValue::readAsChar);
    }

    @Override
    public Result<Double, DecodeError> readAsDouble() {
        return this.readValue(Type.DOUBLE, MemoryValue::readAsDouble);
    }

    @Override
    public Result<Float, DecodeError> readAsFloat() {
        return this.readValue(Type.FLOAT, MemoryValue::readAsFloat);
    }

    @Override
    public Result<Integer, DecodeError> readAsInt() {
        return this.readValue(Type.INT, MemoryValue::readAsInt);
    }

    @Override
    public Result<Long, DecodeError> readAsLong() {
        return this.readValue(Type.LONG, MemoryValue::readAsLong);
    }

    @Override
    public Result<Short, DecodeError> readAsShort() {
        return this.readValue(Type.SHORT, MemoryValue::readAsShort);
    }

    @Override
    public Result<String, DecodeError> readAsString() {
        return this.readValue(Type.STRING, MemoryValue::readAsString);
    }

    private <T> Result<T, DecodeError> readValue(Type type, Function<MemoryValue, Result<T, DecodeError>> reader) {
        return this.delegate instanceof MemoryValue value ?
            reader.apply(value) :
            DecodeError.typeMismatch(type, this.delegate.type()).asFailure();
    }

    @Override
    public <R> Result<R, DecodeError> readList(R identity, BiFunction<R, ? super In, Result<?, ?>> operator) {
        if (this.delegate instanceof MemoryLinkedList list) {
            return list.readElements(identity, operator);
        } else {
            return DecodeError.typeMismatch(Type.LIST, this.delegate.type()).asFailure();
        }
    }

    @Override
    public <R> Result<R, DecodeError> readMap(R identity, BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
        if (this.delegate instanceof MemoryMap map) {
            return map.readEntries(identity, operator);
        } else {
            return DecodeError.typeMismatch(Type.MAP, this.delegate.type()).asFailure();
        }
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }

    sealed interface Delegated permits MemoryLinkedList, MemoryMap, MemoryValue {

        Type type();

    }
}
