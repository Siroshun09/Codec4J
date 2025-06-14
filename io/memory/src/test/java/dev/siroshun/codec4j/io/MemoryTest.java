package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.testhelper.source.ValueSource;
import dev.siroshun.jfun.result.Result;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

class MemoryTest {

    @Test
    void testBoolean() {
        ValueSource.BOOLEAN.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeBoolean(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Boolean, DecodeError> decodeResult = memory.readAsBoolean();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testByte() {
        ValueSource.BYTE.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeByte(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Byte, DecodeError> decodeResult = memory.readAsByte();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testChar() {
        ValueSource.CHAR.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeChar(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Character, DecodeError> decodeResult = memory.readAsChar();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testDouble() {
        ValueSource.DOUBLE.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeDouble(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Double, DecodeError> decodeResult = memory.readAsDouble();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testFloat() {
        ValueSource.FLOAT.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeFloat(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Float, DecodeError> decodeResult = memory.readAsFloat();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testInt() {
        ValueSource.INT.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeInt(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Integer, DecodeError> decodeResult = memory.readAsInt();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testLong() {
        ValueSource.LONG.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeLong(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Long, DecodeError> decodeResult = memory.readAsLong();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testShort() {
        ValueSource.SHORT.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeShort(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<Short, DecodeError> decodeResult = memory.readAsShort();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testString() {
        ValueSource.STRING.values().forEach(value -> {
            Result<Memory, EncodeError> encodeResult = Memory.out().writeString(value);
            Memory memory = ResultAssertions.assertSuccess(encodeResult);

            Result<String, DecodeError> decodeResult = memory.readAsString();
            ResultAssertions.assertSuccess(decodeResult, value);
        });
    }

    @Test
    void testEmptyList() {
        Memory memory = ResultAssertions.assertSuccess(Memory.out().createList().map(ElementAppender::finish).unwrapOr(Result.failure()));
        memory.readList(List.of(), (ignoredList, ignoredIn) -> Assertions.fail("Unexpected call"));
    }

    @Test
    void testSingleList() {
        ElementAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createList());
        ResultAssertions.assertSuccess(appender.append(out -> out.writeInt(1)));
        Memory memory = ResultAssertions.assertSuccess(appender.finish());

        List<Integer> result = ResultAssertions.assertSuccess(memory.readList(new ArrayList<>(1), (list, in) -> in.readAsInt().inspect(list::add)));
        Assertions.assertEquals(List.of(1), result);
    }

    @Test
    void testMultipleList() {
        ElementAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createList());
        ResultAssertions.assertSuccess(appender.append(out -> out.writeInt(1)));
        ResultAssertions.assertSuccess(appender.append(out -> out.writeInt(2)));
        ResultAssertions.assertSuccess(appender.append(out -> out.writeInt(3)));
        Memory memory = ResultAssertions.assertSuccess(appender.finish());

        List<Integer> result = ResultAssertions.assertSuccess(memory.readList(new ArrayList<>(3), (list, in) -> in.readAsInt().inspect(list::add)));
        Assertions.assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void testListAppendFailure() {
        ElementAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createList());
        var failure = new EncodeError.Failure() {
        };
        var appendResult = appender.append(ignored -> failure.asFailure());
        ResultAssertions.assertFailure(appendResult, failure);
    }

    @Test
    void testListIterationError() {
        Memory memory = ResultAssertions.assertSuccess(Memory.out().createList().inspect(appender -> appender.append(o -> o.writeString("a"))).map(ElementAppender::finish).unwrapOr(Result.failure()));
        Result.Failure<?, ?> failure = Result.failure("list iteration error");
        Result<?, DecodeError> result = memory.readList(List.of(), (ignoredList, ignoredIn) -> failure.asFailure());
        ResultAssertions.assertFailure(result, DecodeError.iterationError(failure));
    }

    @Test
    void testEmptyMap() {
        Memory memory = ResultAssertions.assertSuccess(Memory.out().createMap().map(EntryAppender::finish).unwrapOr(Result.failure()));
        memory.readMap(Map.of(), (ignoredMap, ignoredIn) -> Assertions.fail("Unexpected call"));
    }

    @Test
    void testSingleMap() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        ResultAssertions.assertSuccess(appender.append(keyOut -> keyOut.writeInt(1), valueOut -> valueOut.writeInt(2)));
        Memory memory = ResultAssertions.assertSuccess(appender.finish());

        Map<Integer, Integer> result = ResultAssertions.assertSuccess(memory.readMap(new LinkedHashMap<>(1), (map, entryIn) -> {
            int key = ResultAssertions.assertSuccess(entryIn.keyIn().readAsInt());
            int value = ResultAssertions.assertSuccess(entryIn.valueIn().readAsInt());
            map.put(key, value);
            return Result.success();
        }));
        Assertions.assertEquals(Map.of(1, 2), result);
    }

    @Test
    void testMultipleMap() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        ResultAssertions.assertSuccess(appender.append(keyOut -> keyOut.writeInt(1), valueOut -> valueOut.writeInt(2)));
        ResultAssertions.assertSuccess(appender.append(keyOut -> keyOut.writeInt(3), valueOut -> valueOut.writeInt(4)));
        ResultAssertions.assertSuccess(appender.append(keyOut -> keyOut.writeInt(5), valueOut -> valueOut.writeInt(6)));
        Memory memory = ResultAssertions.assertSuccess(appender.finish());
        Map<Integer, Integer> result = ResultAssertions.assertSuccess(memory.readMap(new LinkedHashMap<>(3), (map, entryIn) -> {
            int key = ResultAssertions.assertSuccess(entryIn.keyIn().readAsInt());
            int value = ResultAssertions.assertSuccess(entryIn.valueIn().readAsInt());
            map.put(key, value);
            return Result.success();
        }));
        Assertions.assertEquals(Map.of(1, 2, 3, 4, 5, 6), result);
    }

    @Test
    void testMapKeyAppendFailure() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        var failure = new EncodeError.Failure() {
        };
        var appendResult = appender.append(ignoredKey -> failure.asFailure(), ignoredValue -> Assertions.fail("Unexpected call"));
        ResultAssertions.assertFailure(appendResult, failure);
    }

    @Test
    void testMapValueAppendFailure() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        var failure = new EncodeError.Failure() {
        };
        var appendResult = appender.append(ignoredKey -> Result.success(), ignoredValue -> failure.asFailure());
        ResultAssertions.assertFailure(appendResult, failure);
    }

    @Test
    void testMapIterationError() {
        Memory memory = ResultAssertions.assertSuccess(Memory.out().createMap().inspect(appender -> appender.append(k -> k.writeInt(1), v -> v.writeInt(2))).map(EntryAppender::finish).unwrapOr(Result.failure()));
        Result.Failure<?, ?> failure = Result.failure("map iteration error");
        Result<?, DecodeError> result = memory.readMap(Map.of(), (ignoredMap, ignoredIn) -> failure.asFailure());
        ResultAssertions.assertFailure(result, DecodeError.iterationError(failure));
    }

    @ParameterizedTest
    @MethodSource("writersByType")
    void testType(Map.Entry<Type, Function<Out<Memory>, Result<Memory, EncodeError>>> testCase) {
        Memory memory = ResultAssertions.assertSuccess(testCase.getValue().apply(Memory.out()));
        ResultAssertions.assertSuccess(memory.type(), testCase.getKey());
    }

    @ParameterizedTest
    @MethodSource("writersByType")
    void testTypeMismatch(Map.Entry<Type, Function<Out<Memory>, Result<Memory, EncodeError>>> testCase) {
        Memory memory = ResultAssertions.assertSuccess(testCase.getValue().apply(Memory.out()));
        for (Type type : Type.knownTypes()) {
            if (type.equals(testCase.getKey())) {
                ResultAssertions.assertSuccess(readAs(type, memory));
            } else {
                DecodeError error = ResultAssertions.assertFailure(readAs(type, memory));
                Assertions.assertInstanceOf(DecodeError.TypeMismatch.class, error);
            }
        }
    }

    private static Stream<Map.Entry<Type, Function<Out<Memory>, Result<Memory, EncodeError>>>> writersByType() {
        return Map.<Type, Function<Out<Memory>, Result<Memory, EncodeError>>>ofEntries(
            Map.entry(Type.BOOLEAN, out -> out.writeBoolean(true)),
            Map.entry(Type.BYTE, out -> out.writeByte((byte) 1)),
            Map.entry(Type.CHAR, out -> out.writeChar('a')),
            Map.entry(Type.DOUBLE, out -> out.writeDouble(3.14)),
            Map.entry(Type.FLOAT, out -> out.writeFloat(3.14f)),
            Map.entry(Type.INT, out -> out.writeInt(1)),
            Map.entry(Type.LONG, out -> out.writeLong(1L)),
            Map.entry(Type.SHORT, out -> out.writeShort((short) 1)),
            Map.entry(Type.STRING, out -> out.writeString("a")),
            Map.entry(Type.LIST, out -> out.createList().map(ElementAppender::finish).unwrapOr(Result.failure())),
            Map.entry(Type.MAP, out -> out.createMap().map(EntryAppender::finish).unwrapOr(Result.failure()))
        ).entrySet().stream();
    }

    private static Result<?, DecodeError> readAs(Type type, In in) {
        return switch (type) {
            case Type.BooleanValue ignored -> in.readAsBoolean();
            case Type.ByteValue ignored -> in.readAsByte();
            case Type.CharValue ignored -> in.readAsChar();
            case Type.DoubleValue ignored -> in.readAsDouble();
            case Type.FloatValue ignored -> in.readAsFloat();
            case Type.IntValue ignored -> in.readAsInt();
            case Type.LongValue ignored -> in.readAsLong();
            case Type.ShortValue ignored -> in.readAsShort();
            case Type.StringValue ignored -> in.readAsString();
            case Type.ListType ignored -> in.readList(
                new ArrayList<>(),
                (list, elementIn) -> readAs(elementIn.type().unwrapOr(Type.UNKNOWN), elementIn).map(list::add)
            );
            case Type.MapType ignored -> in.readMap(
                new LinkedHashMap<>(),
                (map, entryIn) -> {
                    var key = readAs(entryIn.keyIn().type().unwrapOr(Type.UNKNOWN), entryIn.keyIn());
                    var value = readAs(entryIn.valueIn().type().unwrapOr(Type.UNKNOWN), entryIn.valueIn());
                    if (key.isFailure() || value.isFailure()) {
                        return Result.failure();
                    }
                    map.put(key.unwrap(), value.unwrap());
                    return Result.success();
                }
            );
            case Type.Unknown ignored -> Result.failure();
        };
    }

    @ParameterizedTest
    @MethodSource("toStringTestCases")
    void testToString(Map.Entry<Function<Out<Memory>, Result<Memory, EncodeError>>, String> testCase) {
        Memory memory = ResultAssertions.assertSuccess(testCase.getKey().apply(Memory.out()));
        Assertions.assertEquals(testCase.getValue(), memory.toString());
    }

    private static Stream<Map.Entry<Function<Out<Memory>, Result<Memory, EncodeError>>, String>> toStringTestCases() {
        //noinspection DataFlowIssue
        return Map.<Function<Out<Memory>, Result<Memory, EncodeError>>, String>ofEntries(
            Map.entry(out -> out.writeBoolean(true), "true"),
            Map.entry(out -> out.writeByte((byte) 1), "1"),
            Map.entry(out -> out.writeChar('a'), "a"),
            Map.entry(out -> out.writeDouble(3.14), "3.14"),
            Map.entry(out -> out.writeFloat(3.14f), "3.14"),
            Map.entry(out -> out.writeInt(1), "1"),
            Map.entry(out -> out.writeLong(1L), "1"),
            Map.entry(out -> out.writeShort((short) 1), "1"),
            Map.entry(out -> out.writeString("a"), "a"),
            Map.entry(out -> out.writeString(null), "null"),
            Map.entry(out -> out.createList().map(ElementAppender::finish).unwrapOr(Result.failure()), "[]"),
            Map.entry(out -> out.createList().inspect(appender -> appender.append(o -> o.writeString("a"))).map(ElementAppender::finish).unwrapOr(Result.failure()), "[a]"),
            Map.entry(out -> out.createList().inspect(appender -> appender.append(o -> o.writeString("a"))).inspect(appender -> appender.append(o -> o.writeString("b"))).map(ElementAppender::finish).unwrapOr(Result.failure()), "[a, b]"),
            Map.entry(out -> out.createMap().map(EntryAppender::finish).unwrapOr(Result.failure()), "{}"),
            Map.entry(out -> out.createMap().inspect(appender -> appender.append(k -> k.writeString("a"), v -> v.writeString("b"))).map(EntryAppender::finish).unwrapOr(Result.failure()), "{a=b}"),
            Map.entry(out -> out.createMap().inspect(appender -> appender.append(k -> k.writeString("a"), v -> v.writeString("b"))).inspect(appender -> appender.append(k -> k.writeString("c"), v -> v.writeString("d"))).map(EntryAppender::finish).unwrapOr(Result.failure()), "{a=b, c=d}")
        ).entrySet().stream();
    }
}
