package dev.siroshun.codec4j.api.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

class DefinedTypesTest {

    @Test
    void testTypes() {
        // types methods in DefinedType and Type return the same list
        Assertions.assertEquals(DefinedTypes.types(), Type.types());
        // No duplicated types
        Assertions.assertEquals(DefinedTypes.types(), new LinkedHashSet<>(DefinedTypes.types()).stream().toList());
    }

}
