package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

@NotNullByDefault
final class MemoryLinkedList implements Memory.Delegated {

    @Contract(" -> new")
    static ElementAppender<Memory> appender() {
        return new Appender();
    }

    private final LinkedNode.Root<Memory> root;

    MemoryLinkedList(LinkedNode.Root<Memory> root) {
        this.root = root;
    }

    @Override
    public Type type() {
        return Type.LIST;
    }

    public ElementReader<Memory> newReader() {
        return new Reader(this.root);
    }

    <R> Result<R, DecodeError> readElements(R identity, BiFunction<R, ? super In, Result<?, ?>> operator) {
        return this.root.iterate(identity, operator);
    }

    @Override
    public String toString() {
        var rootValue = this.root.value();
        if (rootValue == null) {
            return "[]";
        }
        var builder = new StringBuilder("[").append(rootValue);
        var curr = this.root.next();
        while (curr != null) {
            builder.append(", ").append(curr.value());
            curr = curr.next();
        }
        return builder.append(']').toString();
    }

    private static class Appender implements ElementAppender<Memory> {

        private final LinkedNode.Root<Memory> root = new LinkedNode.Root<>();
        private @Nullable LinkedNode<Memory> curr;

        @Override
        public Result<Memory, EncodeError> append(Function<Out<Memory>, Result<Memory, EncodeError>> function) {
            if (this.curr == null) {
                this.curr = this.root;
            } else {
                this.curr = this.curr.createNext();
            }

            var result = function.apply(Memory.out());

            if (result.isSuccess()) {
                this.curr.value(result.unwrap());
            }

            return result;
        }

        @Override
        public Result<Memory, EncodeError> finish() {
            return Result.success(new Memory(new MemoryLinkedList(this.root)));
        }
    }

    private static class Reader implements ElementReader<Memory> {

        private @Nullable LinkedNode<Memory> curr;

        private Reader(LinkedNode<Memory> root) {
            this.curr = root;
        }

        @Override
        public boolean hasNext() {
            return this.curr != null && this.curr.value() != null;
        }

        @Override
        public @NotNull Result<Memory, DecodeError> next() {
            LinkedNode<Memory> curr = this.curr;
            if (curr == null) {
                return DecodeError.noElementError().asFailure();
            }

            Memory value = curr.value();
            if (value == null) {
                return DecodeError.noElementError().asFailure();
            }

            this.curr = curr.next();
            return Result.success(value);
        }

        @Override
        public @NotNull Result<Void, DecodeError> finish() {
            return Result.success();
        }
    }
}
