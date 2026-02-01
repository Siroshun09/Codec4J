package dev.siroshun.codec4j.api.encoder.collection;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.codec.ErrorCodec;
import dev.siroshun.codec4j.testhelper.io.ErrorOut;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

class ListEncoderTest {

    @Test
    void create() {
        Assertions.assertNotNull(ListEncoder.create(Codec.STRING));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void create_null_argument() {
        Assertions.assertThrows(NullPointerException.class, () -> ListEncoder.create(null));
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void encode(TestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<TestCase<?>> testCases() {
        return Stream.of(
            newTestCases("empty", ListEncoder.create(Codec.STRING), List.of(), reader -> Assertions.assertFalse(reader.hasNext())),
            newTestCases("single element", ListEncoder.create(Codec.STRING), List.of("test"), reader -> assertListElement(reader, In::readAsString, "test")),
            newTestCases("multiple elements", ListEncoder.create(Codec.STRING), List.of("a", "b", "c"), reader -> {
                assertListElement(reader, In::readAsString, "a");
                assertListElement(reader, In::readAsString, "b");
                assertListElement(reader, In::readAsString, "c");
            }),
            newTestCases("nested list", ListEncoder.create(ListEncoder.create(Codec.STRING)),
                List.of(List.of("a", "b"), List.of("c"), List.of("d", "e", "f"), List.of(), List.of("g")),
                reader -> {
                    assertNestedList(reader, In::readList, nested -> {
                        assertListElement(nested, In::readAsString, "a");
                        assertListElement(nested, In::readAsString, "b");
                    });
                    assertNestedList(reader, In::readList, nested -> assertListElement(nested, In::readAsString, "c"));
                    assertNestedList(reader, In::readList, nested -> {
                        assertListElement(nested, In::readAsString, "d");
                        assertListElement(nested, In::readAsString, "e");
                        assertListElement(nested, In::readAsString, "f");
                    });
                    assertNestedList(reader, In::readList, nested -> Assertions.assertFalse(nested.hasNext()));
                    assertNestedList(reader, In::readList, nested -> assertListElement(nested, In::readAsString, "g"));
                })
        );
    }

    private static <T> TestCase<T> newTestCases(String name, Encoder<List<T>> encoder, List<T> input, Consumer<ElementReader<? extends In>> assertion) {
        return new TestCase<>(name, encoder, input, assertion);
    }

    private record TestCase<T>(String name, Encoder<List<T>> encoder,
                               List<T> input, Consumer<ElementReader<? extends In>> assertion) {
        @Override
        public @NonNull String toString() {
            return this.name;
        }

        private void doTest() {
            Memory encoded = ResultAssertions.assertSuccess(this.encoder.encode(Memory.out(), this.input));

            ElementReader<? extends In> reader = ResultAssertions.assertSuccess(encoded.readList());
            this.assertion.accept(reader);
            Assertions.assertFalse(reader.hasNext());
        }
    }

    private static <I extends In, T> void assertListElement(ElementReader<I> reader, Function<I, Result<T, DecodeError>> read, T expected) {
        ResultAssertions.assertSuccess(read.apply(ResultAssertions.assertSuccess(reader.next())), expected);
    }

    private static <I extends In> void assertNestedList(ElementReader<I> reader, Function<I, Result<ElementReader<? extends In>, DecodeError>> read, Consumer<ElementReader<? extends In>> assertion) {
        ElementReader<? extends In> listReader = ResultAssertions.assertSuccess(read.apply(ResultAssertions.assertSuccess(reader.next())));
        assertion.accept(listReader);
        Assertions.assertFalse(listReader.hasNext());
    }

    @Test
    void encode_failure() {
        EncodeError error = EncodeError.failure("error");

        ResultAssertions.assertFailure(
            ListEncoder.create(ErrorCodec.encoder(error)).encode(Memory.out(), List.of("test")),
            error
        );

        ResultAssertions.assertFailure(
            ListEncoder.create(Codec.STRING).encode(ErrorOut.create(error), List.of("test")),
            error
        );
    }
}
