package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;

import java.util.stream.Stream;

public interface Source<T> {

    static Stream<Source<?>> allSources() {
        return fromValueSources(ValueSource.sources().stream());
    }

    static Stream<Source<?>> fromValueSources(Stream<ValueSource<?>> valueSources) {
        return valueSources.mapMulti((source, downstream) -> {
            downstream.accept(source);
            downstream.accept(ListSource.fromValueSource(source));
            ListSource.fromValueSource(source).toNestedListSources().forEach(downstream);
            downstream.accept(MapSource.fromSourceWithStringKey(source));
            downstream.accept(MapSource.fromSourceWithStringKey(MapSource.fromSourceWithStringKey(source)));
        });
    }

    Stream<T> values();

    Codec<T> codec();

}
