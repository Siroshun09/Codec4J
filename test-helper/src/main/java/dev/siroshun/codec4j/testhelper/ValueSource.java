package dev.siroshun.codec4j.testhelper;

import dev.siroshun.codec4j.api.io.Type;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.function.Supplier;
import java.util.stream.Stream;

@NotNullByDefault
public record ValueSource<T>(Type.Value<T> type, Supplier<Stream<T>> valuesSupplier) {

    public static final ValueSource<Boolean> BOOLEAN = new ValueSource<>(Type.BOOLEAN, () -> Stream.of(true, false));

    public static final ValueSource<Byte> BYTE = new ValueSource<>(Type.BYTE, () -> Stream.of(Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE).map(Number::byteValue));

    public static final ValueSource<Character> CHAR = new ValueSource<>(Type.CHAR, () -> Stream.of('a', '\n', ' '));

    public static final ValueSource<Double> DOUBLE = new ValueSource<>(Type.DOUBLE, () -> Stream.of(Double.MIN_VALUE, -3.14, -0.0, 0.0, 3.14, Double.MAX_VALUE));

    public static final ValueSource<Float> FLOAT = new ValueSource<>(Type.FLOAT, () -> Stream.of(Float.MIN_VALUE, -3.14f, -0.0f, 0.0f, 3.14f, Float.MAX_VALUE));

    public static final ValueSource<Integer> INT = new ValueSource<>(Type.INT, () -> Stream.of(Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE));

    public static final ValueSource<Long> LONG = new ValueSource<>(Type.LONG, () -> Stream.of(Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE));

    public static final ValueSource<Short> SHORT = new ValueSource<>(Type.SHORT, () -> Stream.of(Short.MIN_VALUE, -1, 0, 1, Short.MAX_VALUE).map(Number::shortValue));

    public static final ValueSource<String> STRING = new ValueSource<>(Type.STRING, () -> Stream.of("a", "abc", " ", ""));


    public Stream<T> values() {
        return this.valuesSupplier.get();
    }

}
