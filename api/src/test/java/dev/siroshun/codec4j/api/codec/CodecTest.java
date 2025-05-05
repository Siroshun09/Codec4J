package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
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
        return Stream.of(
            Stream.of(true, false).map(value -> valueCodecTestCase(Codec.BOOLEAN, value)),
            Stream.of(Byte.MIN_VALUE, (byte) -1, (byte) 0, (byte) 1, Byte.MAX_VALUE).map(value -> valueCodecTestCase(Codec.BYTE, value)),
            Stream.of(Character.MIN_VALUE, 'a', '0', '!', 'あ', Character.MAX_VALUE).map(value -> valueCodecTestCase(Codec.CHAR, value)),
            Stream.of(Double.MIN_VALUE, -1.0, 0.0, 1.0, Math.PI, Double.MAX_VALUE).map(value -> valueCodecTestCase(Codec.DOUBLE, value)),
            Stream.of(Float.MIN_VALUE, -1.0f, 0.0f, 1.0f, (float) Math.PI, Float.MAX_VALUE).map(value -> valueCodecTestCase(Codec.FLOAT, value)),
            Stream.of(Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE).map(value -> valueCodecTestCase(Codec.INT, value)),
            Stream.of(Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE).map(value -> valueCodecTestCase(Codec.LONG, value)),
            Stream.of(Short.MIN_VALUE, (short) -1, (short) 0, (short) 1, Short.MAX_VALUE).map(value -> valueCodecTestCase(Codec.SHORT, value)),
            Stream.of("", "hello", "こんにちは", "1234", "!@#$%^&*()", "\n\r\n\t").map(value -> valueCodecTestCase(Codec.STRING, value))
        ).flatMap(Function.identity());
    }

    private static <T> ValueCodecTestCase<T> valueCodecTestCase(Codec<T> codec, T value) {
        return new ValueCodecTestCase<>(codec, value);
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

    private record ValueCodecTestCase<T>(Codec<T> codec, T value) {
        private void test() {
            Result<Memory, EncodeError> encodeResult = this.codec.encode(Memory.out(), this.value);
            ResultAssertions.assertSuccess(encodeResult);

            Result<T, DecodeError> decodeResult = this.codec.decode(encodeResult.unwrap());
            ResultAssertions.assertSuccess(decodeResult, this.value);
        }
    }
}
