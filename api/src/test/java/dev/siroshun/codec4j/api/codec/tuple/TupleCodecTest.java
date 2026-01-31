package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class TupleCodecTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void testEncodeAndDecode(TestCase testCase) {
        List<String> list = new ArrayList<>(testCase.size());
        for (int i = 0; i < testCase.size(); i++) {
            list.add("test" + i);
        }

        Memory encoded = ResultAssertions.assertSuccess(testCase.codec().encode(Memory.out(), list));
        ResultAssertions.assertSuccess(testCase.codec().decode(encoded), list);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void testToString(TestCase testCase) {
        StringBuilder expected = new StringBuilder("TupleCodec").append(testCase.size()).append("[");
        for (int i = 0; i < testCase.size(); i++) {
            if (i != 0) expected.append(",");
            expected.append("String");
        }
        expected.append("]");
        Assertions.assertEquals(expected.toString(), testCase.codec().toString());
    }

    private static Stream<TestCase> testCases() {
        return Stream.of(
            new TestCase(1, TupleCodec.create(List::of, indexAt(0))),
            new TestCase(2, TupleCodec.create(List::of, indexAt(0), indexAt(1))),
            new TestCase(3, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2))),
            new TestCase(4, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3))),
            new TestCase(5, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4))),
            new TestCase(6, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4), indexAt(5))),
            new TestCase(7, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4), indexAt(5), indexAt(6))),
            new TestCase(8, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4), indexAt(5), indexAt(6), indexAt(7))),
            new TestCase(9, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4), indexAt(5), indexAt(6), indexAt(7), indexAt(8))),
            new TestCase(10, TupleCodec.create(List::of, indexAt(0), indexAt(1), indexAt(2), indexAt(3), indexAt(4), indexAt(5), indexAt(6), indexAt(7), indexAt(8), indexAt(9)))
        );
    }

    private static TupleValueCodec<List<String>, String> indexAt(int index) {
        return TupleValueCodec.create(Codec.STRING, list -> list.get(index));
    }

    private record TestCase(int size, Codec<List<String>> codec) {
    }

}
