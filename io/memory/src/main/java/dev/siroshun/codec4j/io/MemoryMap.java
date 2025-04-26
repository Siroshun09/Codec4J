package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

@NotNullByDefault
final class MemoryMap implements Memory.Delegated {

    @Contract(" -> new")
    static EntryAppender<Memory> appender() {
        return new Appender();
    }

    private final LinkedNode.Root<Entry> root;

    MemoryMap(LinkedNode.Root<Entry> root) {
        this.root = root;
    }

    @Override
    public Type type() {
        return Type.MAP;
    }

    <R> Result<R, DecodeError> readEntries(R identity, BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
        return this.root.iterate(identity, operator);
    }

    @Override
    public String toString() {
        var rootEntry = this.root.value();
        if (rootEntry == null) {
            return "{}";
        }
        var builder = new StringBuilder("{").append(rootEntry);
        var curr = this.root.next();
        while (curr != null) {
            builder.append(", ").append(curr.value());
            curr = curr.next();
        }
        return builder.append('}').toString();
    }

    record Entry(Memory keyMemory, Memory valueMemory) implements EntryIn {

        @Override
        public In keyIn() {
            return this.keyMemory;
        }

        @Override
        public In valueIn() {
            return this.valueMemory;
        }

        @Override
        public String toString() {
            return this.keyMemory + "=" + this.valueMemory;
        }
    }

    private static final class Appender implements EntryAppender<Memory> {

        private final LinkedNode.Root<Entry> root = new LinkedNode.Root<>();
        private @Nullable LinkedNode<Entry> curr;

        @Override
        public Result<Void, EncodeError> append(Function<Out<Memory>, Result<Memory, EncodeError>> keyWriter, Function<Out<Memory>, Result<Memory, EncodeError>> valueWriter) {
            if (this.curr == null) {
                this.curr = this.root;
            } else {
                this.curr = this.curr.createNext();
            }

            var keyResult = keyWriter.apply(Memory.out());

            if (keyResult.isFailure()) {
                return keyResult.asFailure();
            }

            var valueResult = valueWriter.apply(Memory.out());

            if (valueResult.isFailure()) {
                return valueResult.asFailure();
            }

            this.curr.value(new Entry(keyResult.unwrap(), valueResult.unwrap()));

            return Result.success();
        }

        @Override
        public Result<Memory, EncodeError> finish() {
            return Result.success(new Memory(new MemoryMap(this.root)));
        }
    }
}
