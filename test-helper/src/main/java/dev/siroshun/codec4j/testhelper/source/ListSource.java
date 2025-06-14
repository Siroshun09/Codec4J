package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ListSource<E>(Supplier<Stream<E>> valueStreamSupplier, Codec<List<E>> codec) implements Source<List<E>>{

    public static <E> ListSource<E> fromValueSource(ValueSource<E> source) {
        return new ListSource<>(
            source::values,
            Codec.byValueType(source.type()).toListCodec()
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
                this.codec.toListCodec()
            ));
    }
}
