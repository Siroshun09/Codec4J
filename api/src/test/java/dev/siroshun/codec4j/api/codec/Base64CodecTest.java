package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.CodecTester;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

class Base64CodecTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void testEncodeAndDecode(byte[] data) {
        CodecTester.test(Base64Codec.CODEC, data, actual -> Assertions.assertArrayEquals(data, actual));
    }

    private static Stream<byte[]> testCases() {
        return Stream.of(
            new byte[0],
            new byte[128],
            "test".getBytes(StandardCharsets.UTF_8),
            "テスト".getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void testInvalidBase64() {
        Memory invalidB64Memory = Memory.out().writeString("a").unwrap();
        ResultAssertions.assertFailure(Base64Codec.CODEC.decode(invalidB64Memory), new Base64Codec.InvalidBase64Error("a"));
    }
}
