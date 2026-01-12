package dev.siroshun.codec4j.api.encoder.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.ListCodec;
import dev.siroshun.codec4j.api.codec.MapCodec;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.codec.ErrorCodec;
import dev.siroshun.codec4j.testhelper.io.ErrorOut;
import dev.siroshun.codec4j.testhelper.record.TestDataRecord;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

class TupleEncoderTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void encode(TestCase testCase) {
        Memory encoded = ResultAssertions.assertSuccess(testCase.encoder.encode(Memory.out(), TestDataRecord.INSTANCE));

        ElementReader<? extends In> reader = ResultAssertions.assertSuccess(encoded.readList());
        testCase.assertion.accept(reader);
        Assertions.assertFalse(reader.hasNext());
    }

    private static Stream<TestCase> testCases() {
        return Stream.of(
            new TestCase(
                "0 element",
                TupleEncoder.create(List.of()),
                _ -> {
                }
            ),
            new TestCase(
                "1 element",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField)
                ),
                encoded -> assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField())
            ),
            new TestCase(
                "2 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                }
            ),
            new TestCase(
                "3 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                }
            ),
            new TestCase(
                "4 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                }
            ),
            new TestCase(
                "5 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                }
            ),
            new TestCase(
                "6 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                    TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                }
            ),
            new TestCase(
                "7 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                    TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField),
                    TupleValueEncoder.create(Codec.DOUBLE, TestDataRecord::doubleField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                    assertListElement(encoded, In::readAsDouble, TestDataRecord.INSTANCE.doubleField());
                }
            ),
            new TestCase(
                "8 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                    TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField),
                    TupleValueEncoder.create(Codec.DOUBLE, TestDataRecord::doubleField),
                    TupleValueEncoder.create(Codec.FLOAT, TestDataRecord::floatField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                    assertListElement(encoded, In::readAsDouble, TestDataRecord.INSTANCE.doubleField());
                    assertListElement(encoded, In::readAsFloat, TestDataRecord.INSTANCE.floatField());
                }
            ),
            new TestCase(
                "9 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                    TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField),
                    TupleValueEncoder.create(Codec.DOUBLE, TestDataRecord::doubleField),
                    TupleValueEncoder.create(Codec.FLOAT, TestDataRecord::floatField),
                    TupleValueEncoder.create(Codec.CHAR, TestDataRecord::charField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                    assertListElement(encoded, In::readAsDouble, TestDataRecord.INSTANCE.doubleField());
                    assertListElement(encoded, In::readAsFloat, TestDataRecord.INSTANCE.floatField());
                    assertListElement(encoded, In::readAsChar, TestDataRecord.INSTANCE.charField());
                }
            ),
            new TestCase(
                "10 elements",
                TupleEncoder.create(
                    TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                    TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                    TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                    TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                    TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                    TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField),
                    TupleValueEncoder.create(Codec.DOUBLE, TestDataRecord::doubleField),
                    TupleValueEncoder.create(Codec.FLOAT, TestDataRecord::floatField),
                    TupleValueEncoder.create(Codec.CHAR, TestDataRecord::charField),
                    TupleValueEncoder.create(ListCodec.create(Codec.STRING), TestDataRecord::listField)
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                    assertListElement(encoded, In::readAsDouble, TestDataRecord.INSTANCE.doubleField());
                    assertListElement(encoded, In::readAsFloat, TestDataRecord.INSTANCE.floatField());
                    assertListElement(encoded, In::readAsChar, TestDataRecord.INSTANCE.charField());
                    assertNestedList(encoded, In::readList, reader -> {
                        assertListElement(reader, In::readAsString, "a");
                        assertListElement(reader, In::readAsString, "b");
                    });
                }
            ),
            new TestCase(
                "11 elements",
                TupleEncoder.create(
                    List.of(
                        TupleValueEncoder.create(Codec.STRING, TestDataRecord::stringField),
                        TupleValueEncoder.create(Codec.BOOLEAN, TestDataRecord::booleanField),
                        TupleValueEncoder.create(Codec.BYTE, TestDataRecord::byteField),
                        TupleValueEncoder.create(Codec.SHORT, TestDataRecord::shortField),
                        TupleValueEncoder.create(Codec.INT, TestDataRecord::intField),
                        TupleValueEncoder.create(Codec.LONG, TestDataRecord::longField),
                        TupleValueEncoder.create(Codec.DOUBLE, TestDataRecord::doubleField),
                        TupleValueEncoder.create(Codec.FLOAT, TestDataRecord::floatField),
                        TupleValueEncoder.create(Codec.CHAR, TestDataRecord::charField),
                        TupleValueEncoder.create(ListCodec.create(Codec.STRING), TestDataRecord::listField),
                        TupleValueEncoder.create(MapCodec.create(Codec.STRING, Codec.INT), TestDataRecord::mapField)
                    )
                ),
                encoded -> {
                    assertListElement(encoded, In::readAsString, TestDataRecord.INSTANCE.stringField());
                    assertListElement(encoded, In::readAsBoolean, TestDataRecord.INSTANCE.booleanField());
                    assertListElement(encoded, In::readAsByte, TestDataRecord.INSTANCE.byteField());
                    assertListElement(encoded, In::readAsShort, TestDataRecord.INSTANCE.shortField());
                    assertListElement(encoded, In::readAsInt, TestDataRecord.INSTANCE.intField());
                    assertListElement(encoded, In::readAsLong, TestDataRecord.INSTANCE.longField());
                    assertListElement(encoded, In::readAsDouble, TestDataRecord.INSTANCE.doubleField());
                    assertListElement(encoded, In::readAsFloat, TestDataRecord.INSTANCE.floatField());
                    assertListElement(encoded, In::readAsChar, TestDataRecord.INSTANCE.charField());
                    assertNestedList(encoded, In::readList, reader -> {
                        assertListElement(reader, In::readAsString, "a");
                        assertListElement(reader, In::readAsString, "b");
                    });
                    assertNestedMap(encoded, Map.of("a", 1, "b", 2));
                }
            )
        );
    }

    private record TestCase(String name, Encoder<TestDataRecord> encoder,
                            Consumer<ElementReader<? extends In>> assertion) {
        @Override
        public @NonNull String toString() {
            return this.name;
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

    private static <I extends In> void assertNestedMap(ElementReader<I> reader, Map<String, Integer> expected) {
        I elementIn = ResultAssertions.assertSuccess(reader.next());
        Map<String, Integer> map = ResultAssertions.assertSuccess(elementIn.readMap(new HashMap<>(), (m, entryIn) -> {
            String key = ResultAssertions.assertSuccess(entryIn.keyIn().readAsString());
            int value = ResultAssertions.assertSuccess(entryIn.valueIn().readAsInt());
            m.put(key, value);
            return Result.success();
        }));
        Assertions.assertEquals(expected, map);
    }

    @Test
    void encode_failure() {
        EncodeError error = EncodeError.failure("error");

        ResultAssertions.assertFailure(
            TupleEncoder.create(List.of(TupleValueEncoder.create(ErrorCodec.encoder(error), Function.identity()))).encode(Memory.out(), "test"),
            error
        );

        ResultAssertions.assertFailure(
            TupleEncoder.create(List.of(TupleValueEncoder.create(Codec.STRING, Function.identity()))).encode(ErrorOut.create(error), "test"),
            error
        );
    }
}
