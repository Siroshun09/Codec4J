package dev.siroshun.codec4j.api.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class DefinedTypes {

    private static final List<Type> TYPES = new ArrayList<>();

    static <T extends Type> T defineType(T type) {
        TYPES.add(type);
        return type;
    }

    static List<Type> types() {
        return Collections.unmodifiableList(TYPES);
    }
}
