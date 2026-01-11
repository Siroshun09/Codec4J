package dev.siroshun.codec4j.api.codec.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.decoder.tuple.TupleValueDecoder;
import dev.siroshun.codec4j.api.encoder.tuple.TupleValueEncoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.io.ErrorElementReader;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class TupleValueCodecTest {

    @Test
    void create() {
        Assertions.assertNotNull(TupleValueCodec.create(
            TupleValueEncoder.create(Codec.STRING, Function.identity()),
            TupleValueDecoder.create(Codec.STRING)
        ));
        Assertions.assertNotNull(TupleValueCodec.create(Codec.STRING, Function.identity()));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void create_null_arguments() {
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create((TupleValueEncoder<String>) null, (TupleValueDecoder<String>) null));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create(null, TupleValueDecoder.create(Codec.STRING)));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create(TupleValueEncoder.create(Codec.STRING, Function.identity()), (TupleValueDecoder<String>) null));

        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create(null, (Function<String, String>) null));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create(null, Function.identity()));
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueCodec.create(Codec.STRING, null));
    }

    @Test
    void encodeValue() {
        {
            TupleValueCodec<String, String> encoder = TupleValueCodec.create(Codec.STRING, Function.identity());
            Memory encoded = ResultAssertions.assertSuccess(encoder.encodeValue(Memory.out(), "test"));
            ResultAssertions.assertSuccess(encoded.readAsString(), "test");
        }

        {
            TupleValueCodec<Integer, String> encoder = TupleValueCodec.create(Codec.STRING, Integer::toHexString);
            Memory encoded = ResultAssertions.assertSuccess(encoder.encodeValue(Memory.out(), 128));
            ResultAssertions.assertSuccess(encoded.readAsString(), "80");
        }
    }

    @Test
    void decodeFromElementReader() {
        Memory listMemory = Memory.out().createList().flatMap(appender -> {
            ResultAssertions.assertSuccess(appender.append(o -> o.writeString("test")));
            return appender.finish();
        }).unwrap();
        ElementReader<?> reader = listMemory.readList().unwrap();
        ResultAssertions.assertSuccess(
            TupleValueCodec.create(Codec.STRING, Function.identity()).decodeFromElementReader(reader, 0),
            "test"
        );
    }

    @Test
    void decodeFromElementReader_emptyList() {
        Memory emptyListMemory = Memory.out().createList().flatMap(ElementAppender::finish).unwrap();
        ElementReader<?> reader = emptyListMemory.readList().unwrap();
        ResultAssertions.assertFailure(
            TupleValueCodec.create(Codec.STRING, Function.identity()).decodeFromElementReader(reader, 0),
            DecodeError.noElementError(0)
        );
    }

    @Test
    void decodeFromElementReader_nextFailure() {
        DecodeError error = DecodeError.failure("error");
        ResultAssertions.assertFailure(
            TupleValueCodec.create(Codec.STRING, Function.identity()).decodeFromElementReader(ErrorElementReader.create(true, error), 0),
            error
        );
    }

}
