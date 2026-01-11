package dev.siroshun.codec4j.api.decoder.tuple;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.ElementReader;
import dev.siroshun.codec4j.io.Memory;
import dev.siroshun.codec4j.testhelper.io.ErrorElementReader;
import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TupleValueDecoderTest {

    @Test
    void create() {
        Assertions.assertNotNull(TupleValueDecoder.create(Codec.STRING));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void create_null_arguments() {
        Assertions.assertThrows(NullPointerException.class, () -> TupleValueDecoder.create(null));
    }

    @Test
    void decodeFromElementReader() {
        Memory listMemory = Memory.out().createList().flatMap(appender -> {
            ResultAssertions.assertSuccess(appender.append(o -> o.writeString("test")));
            return appender.finish();
        }).unwrap();
        ElementReader<?> reader = listMemory.readList().unwrap();
        ResultAssertions.assertSuccess(
            TupleValueDecoder.create(Codec.STRING).decodeFromElementReader(reader, 0),
            "test"
        );
    }

    @Test
    void decodeFromElementReader_emptyList() {
        Memory emptyListMemory = Memory.out().createList().flatMap(ElementAppender::finish).unwrap();
        ElementReader<?> reader = emptyListMemory.readList().unwrap();
        ResultAssertions.assertFailure(
            TupleValueDecoder.create(Codec.STRING).decodeFromElementReader(reader, 0),
            DecodeError.noElementError(0)
        );
    }

    @Test
    void decodeFromElementReader_nextFailure() {
        DecodeError error = DecodeError.failure("error");
        ResultAssertions.assertFailure(
            TupleValueDecoder.create(Codec.STRING).decodeFromElementReader(ErrorElementReader.create(true, error), 0),
            error
        );
    }
}
