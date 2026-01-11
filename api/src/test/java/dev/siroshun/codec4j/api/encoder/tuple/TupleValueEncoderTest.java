package dev.siroshun.codec4j.api.encoder.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class TupleValueEncoderTest {

    @Test
    void create() {
        Assertions.assertNotNull(TupleValueEncoder.create(Codec.STRING, Function.identity()));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void create_null_arguments() {
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueEncoder.create(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueEncoder.create(null, Function.identity()));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueEncoder.create(Codec.STRING, null));
    }

    @Test
    void encodeValue() {
        {
            TupleValueEncoder<String> encoder = TupleValueEncoder.create(Codec.STRING, Function.identity());
            Memory encoded = ResultAssertions.assertSuccess(encoder.encodeValue(Memory.out(), "test"));
            ResultAssertions.assertSuccess(encoded.readAsString(), "test");
        }

        {
            TupleValueEncoder<Integer> encoder = TupleValueEncoder.create(Codec.STRING, Integer::toHexString);
            Memory encoded = ResultAssertions.assertSuccess(encoder.encodeValue(Memory.out(), 128));
            ResultAssertions.assertSuccess(encoded.readAsString(), "80");
        }
    }
}
