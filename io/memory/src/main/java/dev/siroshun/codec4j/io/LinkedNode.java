package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;

@NotNullByDefault
abstract class LinkedNode<T> {

    abstract void value(T value);

    abstract @Nullable T value();

    abstract LinkedNode<T> createNext();

    abstract @Nullable LinkedNode<T> next();

    static class Root<T> extends LinkedNode<T> {
        private int size;
        private boolean overflowed;
        private @Nullable T value;
        private @Nullable Node<T> next;

        @Override
        @Nullable T value() {
            return this.value;
        }

        @Override
        void value(T value) {
            this.value = value;
        }

        @Override
        Node<T> createNext() {
            if (this.next != null) {
                throw new IllegalStateException("Next node already present");
            }

            this.increaseSize();
            this.next = new Node<>(this);
            return this.next;
        }

        @Override
        @Nullable
        LinkedNode<T> next() {
            return this.next;
        }

        <R> Result<R, DecodeError> iterate(R identity, BiFunction<R, ? super T, Result<?, ?>> operator) {
            Objects.requireNonNull(operator);
            if (this.value == null) {
                return Result.success(identity);
            }

            var result = operator.apply(identity, this.value);
            if (result instanceof Result.Failure<?, ?> failure) {
                return DecodeError.iterationError(failure).asFailure();
            }

            var curr = this.next;

            while (curr != null) {
                result = operator.apply(identity, curr.value);
                if (result instanceof Result.Failure<?, ?> failure) {
                    return DecodeError.iterationError(failure).asFailure();
                }

                curr = curr.next;
            }

            return Result.success(identity);
        }

        private void increaseSize() {
            if (!this.overflowed && ++this.size < 0) {
                this.overflowed = true;
            }
        }
    }

    static class Node<T> extends LinkedNode<T> {

        private final Root<T> root;
        private @Nullable T value;
        private @Nullable Node<T> next;

        Node(Root<T> root) {
            this.root = root;
        }

        @Override
        @Nullable T value() {
            return this.value;
        }

        @Override
        void value(T value) {
            this.value = value;
        }

        @Override
        Node<T> createNext() {
            if (this.next != null) {
                throw new IllegalStateException("Next node already present");
            }

            this.root.increaseSize();
            this.next = new Node<>(this.root);
            return this.next;
        }

        @Override
        @Nullable LinkedNode<T> next() {
            return this.next;
        }
    }
}
