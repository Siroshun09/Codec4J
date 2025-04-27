package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.io.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CodecTest {

    @Test
    void byValueType() {
        Assertions.assertEquals(9, Type.valueTypes().size()); // checks that all value types are tested
        Assertions.assertSame(Codec.BOOLEAN, Codec.byValueType(Type.BOOLEAN));
        Assertions.assertSame(Codec.BYTE, Codec.byValueType(Type.BYTE));
        Assertions.assertSame(Codec.CHAR, Codec.byValueType(Type.CHAR));
        Assertions.assertSame(Codec.DOUBLE, Codec.byValueType(Type.DOUBLE));
        Assertions.assertSame(Codec.FLOAT, Codec.byValueType(Type.FLOAT));
        Assertions.assertSame(Codec.INT, Codec.byValueType(Type.INT));
        Assertions.assertSame(Codec.LONG, Codec.byValueType(Type.LONG));
        Assertions.assertSame(Codec.SHORT, Codec.byValueType(Type.SHORT));
        Assertions.assertSame(Codec.STRING, Codec.byValueType(Type.STRING));

        //noinspection DataFlowIssue
        Assertions.assertThrows(NullPointerException.class, () -> Codec.byValueType(null));
    }
}
