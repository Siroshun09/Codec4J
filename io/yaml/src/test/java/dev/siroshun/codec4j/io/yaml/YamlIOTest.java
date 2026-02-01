package dev.siroshun.codec4j.io.yaml;

import dev.siroshun.codec4j.api.file.FileIO;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.testhelper.io.FileIOTestCase;
import dev.siroshun.codec4j.testhelper.io.TextFileIOTest;
import dev.siroshun.codec4j.testhelper.source.Source;
import dev.siroshun.codec4j.testhelper.source.ValueSource;

import java.util.stream.Stream;

class YamlIOTest extends TextFileIOTest {

    @Override
    protected Stream<FileIO> implementations() {
        return Stream.of(YamlIO.DEFAULT);
    }

    @Override
    protected Stream<FileIOTestCase<?>> createTestCases(Stream<FileIO> impls) {
        return impls.flatMap(
            impl -> Source.fromValueSources(
                Stream.concat(
                    ValueSource.sources().stream().filter(source -> source.type() != Type.CHAR && source.type() != Type.FLOAT),
                    Stream.of(
                        new ValueSource<>(Type.CHAR, () -> Stream.of('a', ' ', '\n')),
                        new ValueSource<>(Type.FLOAT, () -> Stream.of(-3.14f, -1.0f, 0.0f, 1.0f, 3.14f))
                    )
                )
            ).flatMap(source -> FileIOTestCase.fromSource(impl, source))
        );
    }
}
