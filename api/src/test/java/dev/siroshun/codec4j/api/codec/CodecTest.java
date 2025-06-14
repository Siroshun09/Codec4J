package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.CodecTester;
import dev.siroshun.codec4j.testhelper.source.ValueSource;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Test
    void named() {
        Codec<String> codec = Codec.codec(Codec.STRING, Codec.STRING);
        NamedCodec<?> named = Assertions.assertInstanceOf(NamedCodec.class, codec.named("test"));
        Assertions.assertSame(codec, named.codec());
        Assertions.assertEquals("test", named.name());
    }

    @Test
    void xmap() {
        Codec<String> codec = Codec.STRING.xmap(String::toUpperCase, String::toLowerCase);
        Memory memory = ResultAssertions.assertSuccess(codec.encode(Memory.out(), "tEsT"));
        ResultAssertions.assertSuccess(memory.readAsString(), "TEST");
        ResultAssertions.assertSuccess(codec.decode(memory), "test");
    }

    @Test
    void flatXmapSuccess() {
        Codec<String> codec = Codec.STRING.flatXmap(
            str -> Result.success(str.toUpperCase()),
            str -> Result.success(str.toLowerCase())
        );
        Memory memory = ResultAssertions.assertSuccess(codec.encode(Memory.out(), "tEsT"));
        ResultAssertions.assertSuccess(memory.readAsString(), "TEST");
        ResultAssertions.assertSuccess(codec.decode(memory), "test");
    }

    @Test
    void flatXmapFailure() {
        Codec<String> codec = Codec.STRING.flatXmap(
            str -> EncodeError.failure("encode error").asFailure(),
            str -> DecodeError.failure("decode error").asFailure()
        );
        ResultAssertions.assertFailure(codec.encode(Memory.out(), "test"), EncodeError.failure("encode error"));
        Memory memory = ResultAssertions.assertSuccess(Memory.out().writeString("TEST"));
        ResultAssertions.assertFailure(codec.decode(memory), DecodeError.failure("decode error"));
    }

    @Test
    void toListCodec() {
        Codec<List<String>> codec = Codec.STRING.toListCodec();
        CodecTester.test(codec, List.of());
        CodecTester.test(codec, List.of("a"));
        CodecTester.test(codec, List.of("a", "b", "c"));
    }

    @Test
    void toSetCodec() {
        Codec<Set<String>> codec = Codec.STRING.toSetCodec();
        CodecTester.test(codec, Set.of());
        CodecTester.test(codec, Set.of("a"));
        CodecTester.test(codec, Set.of("a", "b", "c"));
    }

    @Test
    void toCollectionCodec() {
        Codec<Collection<String>> codec = Codec.STRING.toCollectionCodec();
        CodecTester.test(codec, List.of());
        CodecTester.test(codec, List.of("a"));
        CodecTester.test(codec, List.of("a", "b", "c"));
    }

    @Test
    void toMapCodecAsKey() {
        Codec<Map<String, Integer>> codec = Codec.STRING.toMapCodecAsKey(Codec.INT);
        CodecTester.test(codec, Map.of());
        CodecTester.test(codec, Map.of("a", 1));
        CodecTester.test(codec, Map.of("a", 1, "b", 2, "c", 3));
    }

    @Test
    void toMapCodecAsValue() {
        Codec<Map<Integer, String>> codec = Codec.STRING.toMapCodecAsValue(Codec.INT);
        CodecTester.test(codec, Map.of());
        CodecTester.test(codec, Map.of(1, "a"));
        CodecTester.test(codec, Map.of(1, "a", 2, "b", 3, "c"));
    }
}
