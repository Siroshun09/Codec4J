package dev.siroshun.codec4j.io;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.EntryReader;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.codec4j.testhelper.source.Source;
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
    void testMapKeyAppendFailure() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        var failure = new EncodeError.Failure() {
        };
        var appendResult = appender.append(_ -> failure.asFailure(), _ -> Assertions.fail("Unexpected call"));
        ResultAssertions.assertFailure(appendResult, failure);
    }

    @Test
    void testMapValueAppendFailure() {
        EntryAppender<Memory> appender = ResultAssertions.assertSuccess(Memory.out().createMap());
        var failure = new EncodeError.Failure() {
        };
        var appendResult = appender.append(_ -> Result.success(), _ -> failure.asFailure());
        ResultAssertions.assertFailure(appendResult, failure);
    }

    @ParameterizedTest
    @MethodSource("testCasesForSkip")
    void testSkip(Memory memory) {
        ResultAssertions.assertSuccess(memory.skip());
    }

    private static Stream<Memory> testCasesForSkip() {
        return Stream.of(
            Memory.out().writeBoolean(true),
            Memory.out().writeByte((byte) 1),
            Memory.out().writeChar('a'),
            Memory.out().writeDouble(3.14),
            Memory.out().writeFloat(3.14f),
            Memory.out().writeInt(1),
            Memory.out().writeLong(1L),
            Memory.out().writeShort((short) 1),
            Memory.out().writeString("a"),
            Memory.out().createList().map(ElementAppender::finish).unwrapOr(Result.failure()),
            Memory.out().createList().inspect(appender -> appender.append(in -> in.writeString("a"))).map(ElementAppender::finish).unwrapOr(Result.failure()),
            Memory.out().createMap().map(EntryAppender::finish).unwrapOr(Result.failure()),
            Memory.out().createMap().inspect(appender -> appender.append(k -> k.writeString("a"), v -> v.writeString("b"))).map(EntryAppender::finish).unwrapOr(Result.failure())
        ).map(Result::unwrap);
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
            case Type.BooleanValue _ -> in.readAsBoolean();
            case Type.ByteValue _ -> in.readAsByte();
            case Type.CharValue _ -> in.readAsChar();
            case Type.DoubleValue _ -> in.readAsDouble();
            case Type.FloatValue _ -> in.readAsFloat();
            case Type.IntValue _ -> in.readAsInt();
            case Type.LongValue _ -> in.readAsLong();
            case Type.ShortValue _ -> in.readAsShort();
            case Type.StringValue _ -> in.readAsString();
            case Type.ListType _ -> {
                Result<ElementReader<? extends In>, DecodeError> readerResult = in.readList();
                if (readerResult.isFailure()) {
                    yield readerResult.asFailure();
                }

                ElementReader<? extends In> reader = readerResult.unwrap();
                List<Object> list = new ArrayList<>();
                while (reader.hasNext()) {
                    Result<? extends In, DecodeError> elementResult = reader.next();
                    if (elementResult.isFailure()) {
                        yield elementResult.asFailure();
                    }

                    In elementIn = elementResult.unwrap();
                    Result<?, DecodeError> decodeResult = readAs(elementIn.type().unwrapOr(Type.UNKNOWN), elementIn);
                    if (decodeResult.isFailure()) {
                        yield decodeResult.asFailure();
                    }
                    list.add(decodeResult.unwrap());
                }

                Result<Void, DecodeError> finishResult = reader.finish();
                if (finishResult.isFailure()) {
                    yield finishResult.asFailure();
                }

                yield Result.success(list);
            }
            case Type.MapType _ -> {
                Result<EntryReader, DecodeError> readerResult = in.readMap();
                if (readerResult.isFailure()) {
                    yield readerResult.asFailure();
                }

                EntryReader reader = readerResult.unwrap();
                Map<Object, Object> map = new LinkedHashMap<>();

                while (reader.hasNext()) {
                    Result<EntryIn, DecodeError> entryInResult = reader.next();
                    if (entryInResult.isFailure()) {
                        yield entryInResult.asFailure();
                    }

                    EntryIn entryIn = entryInResult.unwrap();
                    Result<?, DecodeError> key = readAs(entryIn.keyIn().type().unwrapOr(Type.UNKNOWN), entryIn.keyIn());
                    Result<?, DecodeError> value = readAs(entryIn.valueIn().type().unwrapOr(Type.UNKNOWN), entryIn.valueIn());

                    if (key.isFailure()) {
                        yield key.asFailure();
                    } else if (value.isFailure()) {
                        yield value.asFailure();
                    }

                    map.put(key.unwrap(), value.unwrap());
                }

                Result<Void, DecodeError> finishResult = reader.finish();
                if (finishResult.isFailure()) {
                    yield finishResult.asFailure();
                }

                yield Result.success(map);
            }
            case Type.Unknown _ -> Result.failure();
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

    @ParameterizedTest
    @MethodSource("sourceTestCases")
    void testBySource(SourceTestCase<?> testCase) {
        testCase.doTest();
    }

    private static Stream<SourceTestCase<?>> sourceTestCases() {
        return Source.allSources().flatMap(SourceTestCase::createFromSource);
    }

    private record SourceTestCase<T>(Codec<T> codec, T value) {

        private static <T> Stream<SourceTestCase<T>> createFromSource(Source<T> source) {
            return source.values().map(value -> new SourceTestCase<>(source.codec(), value));
        }

        private void doTest() {
            Memory encoded = ResultAssertions.assertSuccess(this.codec.encode(Memory.out(), this.value));
            T decoded = ResultAssertions.assertSuccess(this.codec.decode(encoded));
            Assertions.assertEquals(this.value, decoded);
        }
    }
}
