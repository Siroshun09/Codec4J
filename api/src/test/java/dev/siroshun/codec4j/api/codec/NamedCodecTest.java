package dev.siroshun.codec4j.api.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NamedCodecTest {

    @Test
    void namedDoesNotWrapSelf() {
        NamedCodec<?> originalNamedCodec = Assertions.assertInstanceOf(NamedCodec.class, Codec.STRING.named("test"));
        NamedCodec<?> newNamedCodec = Assertions.assertInstanceOf(NamedCodec.class, originalNamedCodec.named("test2"));
        Assertions.assertNotSame(originalNamedCodec, newNamedCodec.codec());
        Assertions.assertSame(originalNamedCodec.codec(), newNamedCodec.codec());
    }
}
