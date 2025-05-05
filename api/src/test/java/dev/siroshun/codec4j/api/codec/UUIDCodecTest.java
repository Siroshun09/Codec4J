package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.CodecTester;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

class UUIDCodecTest {

    @ParameterizedTest
    @MethodSource("testCasesForAsString")
    void testAsString(UUID uuid) {
        CodecTester.test(UUIDCodec.UUID_AS_STRING, uuid);
    }

    private static Stream<UUID> testCasesForAsString() {
        return Stream.of(
            new UUID(0, 0),
            UUID.fromString("7b59c928-2982-11f0-9cd2-0242ac120002"), // v1
            UUID.fromString("30350a08-9ab1-41d9-b7cb-439bba8b5c14"), // v4
            UUID.fromString("01969f58-7ae9-7c36-bf2c-df3219d7fc3d") // v7
        );
    }

    @Test
    void invalidUUIDFormatTest() {
        Memory invalidUUID = Memory.out().writeString("invalid").unwrap();
        ResultAssertions.assertFailure(UUIDCodec.UUID_AS_STRING.decode(invalidUUID), new UUIDCodec.InvalidUUIDFormatError("invalid"));

        Memory emptyUUID = Memory.out().writeString("").unwrap();
        ResultAssertions.assertFailure(UUIDCodec.UUID_AS_STRING.decode(emptyUUID), new UUIDCodec.InvalidUUIDFormatError(""));
    }
}
