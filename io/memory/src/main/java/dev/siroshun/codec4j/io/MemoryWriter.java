package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.function.BiConsumer;

@NotNullByDefault
final class MemoryWriter implements Out<Memory> {

    static final MemoryWriter INSTANCE = new MemoryWriter();

    private MemoryWriter() {
    }

    @Override
    public Result.Success<Memory, EncodeError> writeBoolean(boolean value) {
        return writeValue(value, MemoryValue::writeBoolean);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeByte(byte value) {
        return writeValue(value, MemoryValue::writeByte);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeChar(char value) {
        return writeValue(value, MemoryValue::writeChar);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeDouble(double value) {
        return writeValue(value, MemoryValue::writeDouble);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeFloat(float value) {
        return writeValue(value, MemoryValue::writeFloat);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeInt(int value) {
        return writeValue(value, MemoryValue::writeInt);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeLong(long value) {
        return writeValue(value, MemoryValue::writeLong);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeShort(short value) {
        return writeValue(value, MemoryValue::writeShort);
    }

    @Override
    public Result.Success<Memory, EncodeError> writeString(String value) {
        return writeValue(value, MemoryValue::writeString);
    }

    private static <T> Result.Success<Memory, EncodeError> writeValue(T value, BiConsumer<MemoryValue, T> writer) {
        var memoryValue = new MemoryValue();
        writer.accept(memoryValue, value);
        return Result.success(new Memory(memoryValue));
    }

    @Override
    public Result<ElementAppender<Memory>, EncodeError> createList() {
        return Result.success(MemoryLinkedList.appender());
    }

    @Override
    public Result<EntryAppender<Memory>, EncodeError> createMap() {
        return Result.success(MemoryMap.appender());
    }

}
