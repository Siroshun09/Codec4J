package dev.siroshun.codec4j.api.decoder.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.codec.ErrorCodec;
import dev.siroshun.codec4j.testhelper.io.ErrorIn;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ListDecoderTest {

    @Test
    void create() {
        Assertions.assertNotNull(ListDecoder.create(Codec.STRING));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void create_null_argument() {
        Assertions.assertThrows(NullPointerException.class, () -> ListDecoder.create(null));
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void decode(TestCase testCase) {
        Memory memory = Memory.out().createList().flatMap(appender -> {
            for (int i = 1; i <= testCase.size(); i++) {
                String value = "test-" + i;
                appender.append(o -> o.writeString(value));
            }
            return appender.finish();
        }).unwrap();

        ResultAssertions.assertSuccess(ListDecoder.create(Codec.STRING).decode(memory), testCase.expected());
    }

    private static Stream<TestCase> testCases() {
        return IntStream.of(0, 1, 2, 5, 10, 100)
            .mapToObj(size -> new TestCase(
                size,
                IntStream.rangeClosed(1, size).mapToObj(i -> "test-" + i).toList()
            ));
    }

    private record TestCase(int size, List<String> expected) {
    }

    @Test
    void decode_failure() {
        DecodeError decodeError = DecodeError.failure("error");
        ResultAssertions.assertFailure(
            ListDecoder.create(Codec.STRING).decode(ErrorIn.create(decodeError)),
            decodeError
        );

        Memory memory = Memory.out().createList().flatMap(appender -> {
            appender.append(o -> o.writeString("test"));
            return appender.finish();
        }).unwrap();
        ResultAssertions.assertFailure(
            ListDecoder.create(ErrorCodec.decoder(decodeError)).decode(memory),
            decodeError
        );
    }

    @Test
    void decode_ignorableError() {
        Memory memory = Memory.out().createList().flatMap(appender -> {
            List.of("test1", "ignore", "test2").forEach(value -> appender.append(o -> o.writeString(value)));
            return appender.finish();
        }).unwrap();

        ResultAssertions.assertSuccess(
            ListDecoder.create(Codec.STRING.flatMap(value ->
                value.equals("ignore") ?
                    DecodeError.failure("error").asIgnorable().asFailure() :
                    Result.success(value)
            )).decode(memory),
            List.of("test1", "test2")
        );
    }
}
