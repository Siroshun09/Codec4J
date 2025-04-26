package dev.siroshun.codec4j.api.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TypeTest {

    @Test
    void knownTypes() {
        Assertions.assertEquals(
                Type.types().stream().filter(type -> type != Type.UNKNOWN).toList(),
                Type.knownTypes()
        );
    }

    @Test
    void valueTypes() {
        Assertions.assertEquals(
                Type.types().stream().filter(type -> type instanceof Type.Value<?>).toList(),
                Type.valueTypes()
        );
    }

    @Test
    void numberTypes() {
        Assertions.assertEquals(
                Type.types().stream().filter(type -> type instanceof Type.NumberValue<?>).toList(),
                Type.numberTypes()
        );
    }

    @ParameterizedTest
    @MethodSource("dev.siroshun.codec4j.api.io.Type#types")
    void testType(Type type) {
        Assertions.assertEquals(type == Type.BOOLEAN, type.isBoolean());
        Assertions.assertEquals(type instanceof Type.NumberValue<?>, type.isNumber());
        Assertions.assertEquals(type == Type.STRING, type.isString());
        Assertions.assertEquals(type == Type.LIST, type.isList());
        Assertions.assertEquals(type == Type.MAP, type.isMap());
        Assertions.assertEquals(type == Type.UNKNOWN, type.isUnknown());
    }
}
