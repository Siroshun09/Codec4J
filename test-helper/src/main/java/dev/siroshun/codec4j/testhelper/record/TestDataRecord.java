package dev.siroshun.codec4j.testhelper.record;

import java.util.List;
import java.util.Map;

public record TestDataRecord(
    String stringField,
    boolean booleanField,
    byte byteField,
    short shortField,
    int intField,
    long longField,
    double doubleField,
    float floatField,
    char charField,
    List<String> listField,
    Map<String, Integer> mapField
) {
    public static final TestDataRecord INSTANCE = new TestDataRecord(
        "test",
        true,
        (byte) 1,
        (short) 2,
        3,
        4L,
        5.0,
        6.0f,
        'c',
        List.of("a", "b"),
        Map.of("a", 1, "b", 2)
    );
}
