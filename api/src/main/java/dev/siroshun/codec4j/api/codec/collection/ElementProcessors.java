package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

final class ElementProcessors {

    static abstract class AbstractProcessor<E, T> implements ElementProcessor<E, T> {

        private final Codec<E> elementCodec;

        AbstractProcessor(Codec<E> elementCodec) {
            this.elementCodec = elementCodec;
        }

        @Override
        public @NotNull <O> Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element) {
            return this.elementCodec.encode(out, element);
        }

        @Override
        public @NotNull Result<E, DecodeError> decodeElement(@NotNull In in) {
            return this.elementCodec.decode(in);
        }
    }

    static final class ListProcessor<E> extends AbstractProcessor<E, List<E>> {

        ListProcessor(Codec<E> elementCodec) {
            super(elementCodec);
        }

        @Override
        public @NotNull Iterator<E> toIterator(@UnknownNullability List<E> input) {
            return Objects.requireNonNull(input).iterator();
        }

        @Override
        public @NotNull List<E> createIdentity() {
            return new ArrayList<>();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptElement(@NotNull List<E> identity, @UnknownNullability E element) {
            identity.add(element);
            return Result.success();
        }

        @Override
        public @NotNull List<E> finalizeIdentity(@NotNull List<E> identity) {
            return identity;
        }
    }

    static final class SetProcessor<E> extends AbstractProcessor<E, Set<E>> {

        SetProcessor(Codec<E> elementCodec) {
            super(elementCodec);
        }

        @Override
        public @NotNull Iterator<E> toIterator(@UnknownNullability Set<E> input) {
            return Objects.requireNonNull(input).iterator();
        }

        @Override
        public @NotNull Set<E> createIdentity() {
            return new HashSet<>();
        }

        @Override
        public @NotNull Result<Void, DecodeError> acceptElement(@NotNull Set<E> identity, @UnknownNullability E element) {
            if (!identity.add(element)) {
                return new CollectionCodec.DuplicatedElementError(element).asFailure();
            }
            return Result.success();
        }

        @Override
        public @NotNull Set<E> finalizeIdentity(@NotNull Set<E> identity) {
            return identity;
        }
    }
}
