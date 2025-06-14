package dev.siroshun.codec4j.io.yaml;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.testhelper.FileIOTestCase;
import dev.siroshun.codec4j.testhelper.source.Source;
import dev.siroshun.codec4j.testhelper.source.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class YamlIOTest {

    @ParameterizedTest
    @MethodSource("testCasesForDefault")
    void testDefault(FileIOTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<FileIOTestCase<?>> testCasesForDefault() {
        return Source.fromValueSources(
            Stream.concat(
                ValueSource.sources().stream().filter(source -> source.type() != Type.CHAR && source.type() != Type.FLOAT),
                Stream.of(
                    new ValueSource<>(Type.CHAR, () -> Stream.of('a', ' ', '\n')),
                    new ValueSource<>(Type.FLOAT, () -> Stream.of(-3.14f, -1.0f, 0.0f, 1.0f, 3.14f))
                )
            )
        ).flatMap(source -> FileIOTestCase.fromSource(YamlIO.DEFAULT, source));
    }
}
