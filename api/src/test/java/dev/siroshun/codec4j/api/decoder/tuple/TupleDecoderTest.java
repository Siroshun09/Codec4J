package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.io.ErrorIn;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class TupleDecoderTest {

    @ParameterizedTest
    @MethodSource("decoders")
    void decode_success(TupleDecoderDefinition def) {
        Memory memory = Memory.out().createList().flatMap(appender -> {
            for (int i = 0; i < def.size(); i++) {
                String value = "test" + i;
                appender.append(o -> o.writeString(value));
            }
            return appender.finish();
        }).unwrap();

        List<String> expected = new ArrayList<>(def.size());
        for (int i = 0; i < def.size(); i++) {
            expected.add("test" + i);
        }

        ResultAssertions.assertSuccess(def.decoder().decode(memory), expected);
    }

    @ParameterizedTest
    @MethodSource("decoders")
    void decode_readList_error(TupleDecoderDefinition def) {
        DecodeError error = DecodeError.failure("error");
        ResultAssertions.assertFailure(
            def.decoder().decode(ErrorIn.create(error)),
            error
        );
    }


    private static Stream<TupleDecoderDefinition> decoders() {
        TupleValueDecoder<String> v = TupleValueDecoder.create(Codec.STRING);
        return Stream.of(
            new TupleDecoderDefinition(1, TupleDecoder.create(List::of, v)),
            new TupleDecoderDefinition(2, TupleDecoder.create(List::of, v, v)),
            new TupleDecoderDefinition(3, TupleDecoder.create(List::of, v, v, v)),
            new TupleDecoderDefinition(4, TupleDecoder.create(List::of, v, v, v, v)),
            new TupleDecoderDefinition(5, TupleDecoder.create(List::of, v, v, v, v, v)),
            new TupleDecoderDefinition(6, TupleDecoder.create(List::of, v, v, v, v, v, v)),
            new TupleDecoderDefinition(7, TupleDecoder.create(List::of, v, v, v, v, v, v, v)),
            new TupleDecoderDefinition(8, TupleDecoder.create(List::of, v, v, v, v, v, v, v, v)),
            new TupleDecoderDefinition(9, TupleDecoder.create(List::of, v, v, v, v, v, v, v, v, v)),
            new TupleDecoderDefinition(10, TupleDecoder.create(List::of, v, v, v, v, v, v, v, v, v, v))
        );
    }

    private record TupleDecoderDefinition(int size, Decoder<List<String>> decoder) {
    }

    @ParameterizedTest
    @MethodSource("noElementTestCases")
    void decode_noElementError(NoElementTestCase testCase) {
        Memory memory = Memory.out().createList().flatMap(appender -> {
            for (int i = 0; i < testCase.elementCount(); i++) {
                String value = "test" + i;
                appender.append(o -> o.writeString(value));
            }
            return appender.finish();
        }).unwrap();

        ResultAssertions.assertFailure(
            testCase.definition().decoder().decode(memory),
            DecodeError.noElementError(testCase.elementCount())
        );
    }

    private static Stream<NoElementTestCase> noElementTestCases() {
        return decoders().mapMulti((def, consumer) -> {
            for (int i = 0, max = def.size() - 1; i < max; i++) {
                consumer.accept(new NoElementTestCase(i, def));
            }
        });
    }

    private record NoElementTestCase(int elementCount, TupleDecoderDefinition definition) {
    }

    @ParameterizedTest
    @MethodSource("elementErrorDecoders")
    void decode_elementError(ElementErrorTestCase testCase) {
        Memory memory = Memory.out().createList().flatMap(appender -> {
            for (int i = 0; i < testCase.definition().size(); i++) {
                String value = "test" + i;
                appender.append(o -> o.writeString(value));
            }
            return appender.finish();
        }).unwrap();

        ResultAssertions.assertFailure(testCase.definition().decoder().decode(memory), testCase.expected());
    }

    private static Stream<ElementErrorTestCase> elementErrorDecoders() {
        return decoders().mapMulti((def, consumer) -> {
            int elementCount = def.size();
            for (int index = 0; index < elementCount; index++) {
                for (int errorAt = 0; errorAt < elementCount; errorAt++) {
                    int failureIndex = index;
                    DecodeError error = DecodeError.failure("error at index " + index);

                    TupleValueDecoder<String> v = (reader, idx) -> TupleValueDecoder.create(Codec.STRING.flatMap(
                        value -> idx == failureIndex ? error.asFailure() : Result.success(value)
                    )).decodeFromElementReader(reader, idx);

                    consumer.accept(new ElementErrorTestCase(
                        new TupleDecoderDefinition(
                            elementCount,
                            switch (elementCount) {
                                case 1 -> TupleDecoder.create(List::of, v);
                                case 2 -> TupleDecoder.create(List::of, v, v);
                                case 3 -> TupleDecoder.create(List::of, v, v, v);
                                case 4 -> TupleDecoder.create(List::of, v, v, v, v);
                                case 5 -> TupleDecoder.create(List::of, v, v, v, v, v);
                                case 6 -> TupleDecoder.create(List::of, v, v, v, v, v, v);
                                case 7 -> TupleDecoder.create(List::of, v, v, v, v, v, v, v);
                                case 8 -> TupleDecoder.create(List::of, v, v, v, v, v, v, v, v);
                                case 9 -> TupleDecoder.create(List::of, v, v, v, v, v, v, v, v, v);
                                case 10 -> TupleDecoder.create(List::of, v, v, v, v, v, v, v, v, v, v);
                                default -> throw new IllegalStateException("Unexpected value: " + elementCount);
                            }
                        ),
                        error
                    ));
                }
            }
        });
    }

    private record ElementErrorTestCase(TupleDecoderDefinition definition, DecodeError expected) {
    }
}
