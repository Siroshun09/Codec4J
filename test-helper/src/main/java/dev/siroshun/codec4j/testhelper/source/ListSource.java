package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.collection.ListCodec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ListSource<E>(Supplier<Stream<E>> valueStreamSupplier, Codec<List<E>> codec) implements Source<List<E>> {

    public static <E> ListSource<E> fromValueSource(ValueSource<E> source) {
        return new ListSource<>(
            source::values,
            ListCodec.create(Codec.byValueType(source.type()))
        );
    }

    public static <E> ListSource<E> fromValueSourceForElementReader(ValueSource<E> source) {
        return new ListSource<>(
            source::values,
            new ElementReaderListCodec<>(Codec.byValueType(source.type()))
        );
    }

    @Override
    public Stream<List<E>> values() {
        return Stream.concat(
            Stream.of(this.empty(), this.byMultipleElements()),
            this.bySingleElement()
        );
    }

    public List<E> empty() {
        return List.of();
    }

    public Stream<List<E>> bySingleElement() {
        return this.valueStreamSupplier.get().map(List::of);
    }

    public List<E> byMultipleElements() {
        return this.valueStreamSupplier.get().toList();
    }

    public Stream<List<List<E>>> allNestedListCase() {
        Stream.Builder<List<List<E>>> builder = Stream.builder();
        builder.add(List.of());
        this.values().map(List::of).forEach(builder::add); // single nested list
        builder.add(this.values().toList()); // multiple nested list
        return builder.build();
    }

    Stream<ListSource<List<E>>> toNestedListSources() {
        return this.allNestedListCase()
            .map(list -> new ListSource<>(
                list::stream,
                ListCodec.create(this.codec)
            ));
    }

    public record ElementReaderListCodec<E>(@NotNull Codec<E> elementCodec) implements Codec<List<E>> {

        @Override
        public @NotNull Result<List<E>, DecodeError> decode(@NotNull In in) {
            Result<ElementReader<? extends In>, DecodeError> readerResult = in.readList();
            if (readerResult.isFailure()) {
                return readerResult.asFailure();
            }

            ElementReader<? extends In> reader = readerResult.unwrap();
            List<E> list = new ArrayList<>();
            while (reader.hasNext()) {
                Result<? extends In, DecodeError> elementIn = reader.next();
                if (elementIn.isFailure()) {
                    return elementIn.asFailure();
                }

                Result<E, DecodeError> decodeResult = this.elementCodec.decode(elementIn.unwrap());
                if (decodeResult.isFailure()) {
                    return decodeResult.asFailure();
                }

                list.add(decodeResult.unwrap());
            }

            Result<Void, DecodeError> finishResult = reader.finish();
            if (finishResult.isFailure()) {
                return finishResult.asFailure();
            }

            return Result.success(list);
        }

        @Override
        public @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability List<E> input) {
            return ListCodec.create(this.elementCodec).encode(out, input);
        }
    }
}
