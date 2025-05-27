package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.CodecTester;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class EnumCodecTest {

    @ParameterizedTest
    @MethodSource("values")
    void testByName(TestEnum enumValue) {
        CodecTester.test(EnumCodec.byName(TestEnum.class), enumValue);
    }

    @Test
    void testUnknownEnumName() {
        Memory unknownMemory = Memory.out().writeString("unknown").unwrap();
        ResultAssertions.assertFailure(
            EnumCodec.byName(TestEnum.class).decode(unknownMemory),
            new EnumCodec.UnknownEnumNameDecodeError(TestEnum.class, "unknown")
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void testByOrdinal(TestEnum enumValue) {
        CodecTester.test(EnumCodec.byOrdinal(TestEnum.class), enumValue);
    }

    @Test
    void testUnknownOrdinal() {
        Memory unknownMemory = Memory.out().writeInt(TestEnum.values().length).unwrap();
        ResultAssertions.assertFailure(
            EnumCodec.byOrdinal(TestEnum.class).decode(unknownMemory),
            new EnumCodec.UnknownEnumOrdinalDecodeError(TestEnum.class, TestEnum.values().length)
        );
    }

    private static Stream<TestEnum> values() {
        return Stream.of(TestEnum.values());
    }

    private enum TestEnum {
        A, B, C
    }
}
