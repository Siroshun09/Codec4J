package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.testhelper.CodecTester;
import dev.siroshun.codec4j.testhelper.ValueSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class CodecTest {

    @Test
    void byValueType() {
        Assertions.assertEquals(9, Type.valueTypes().size()); // checks that all value types are tested
        Assertions.assertSame(Codec.BOOLEAN, Codec.byValueType(Type.BOOLEAN));
        Assertions.assertSame(Codec.BYTE, Codec.byValueType(Type.BYTE));
        Assertions.assertSame(Codec.CHAR, Codec.byValueType(Type.CHAR));
        Assertions.assertSame(Codec.DOUBLE, Codec.byValueType(Type.DOUBLE));
        Assertions.assertSame(Codec.FLOAT, Codec.byValueType(Type.FLOAT));
        Assertions.assertSame(Codec.INT, Codec.byValueType(Type.INT));
        Assertions.assertSame(Codec.LONG, Codec.byValueType(Type.LONG));
        Assertions.assertSame(Codec.SHORT, Codec.byValueType(Type.SHORT));
        Assertions.assertSame(Codec.STRING, Codec.byValueType(Type.STRING));

        //noinspection DataFlowIssue
        Assertions.assertThrows(NullPointerException.class, () -> Codec.byValueType(null));
    }

    @ParameterizedTest
    @MethodSource("valueCodecTestCases")
    void testValueCodec(ValueCodecTestCase<?> testCase) {
        testCase.test();
    }

    private static Stream<ValueCodecTestCase<?>> valueCodecTestCases() {
        return ValueSource.sources().stream().flatMap(CodecTest::valueCodecTestCaseFromValueSource);
    }

    private static <T> Stream<ValueCodecTestCase<T>> valueCodecTestCaseFromValueSource(ValueSource<T> source) {
        Codec<T> codec = Codec.byValueType(source.type());
        return source.values().map(value -> new ValueCodecTestCase<>(codec, value));
    }

    private record ValueCodecTestCase<T>(Codec<T> codec, T value) {
        private void test() {
            CodecTester.test(this.codec, this.value);
        }
    }

    @Test
    void codec() {
        SimpleCodec<?> codec = Assertions.assertInstanceOf(SimpleCodec.class, Codec.codec(Codec.STRING, Codec.STRING));
        Assertions.assertEquals(Codec.STRING, codec.encoder());
        Assertions.assertEquals(Codec.STRING, codec.decoder());

        NamedCodec<?> named = Assertions.assertInstanceOf(NamedCodec.class, Codec.codec(Codec.STRING, Codec.STRING, "test"));
        Assertions.assertEquals(Codec.STRING, codec.encoder());
        Assertions.assertEquals(Codec.STRING, codec.decoder());
        Assertions.assertEquals("test", named.name());
    }
}
