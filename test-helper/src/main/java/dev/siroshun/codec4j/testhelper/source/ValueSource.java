package dev.siroshun.codec4j.testhelper.source;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.io.Type;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NotNullByDefault
public record ValueSource<T>(Type.Value<T> type, Supplier<Stream<T>> valuesSupplier) implements Source<T> {

    private static final List<ValueSource<?>> SOURCES = new ArrayList<>();

    private static <T> ValueSource<T> create(Type.Value<T> type, Supplier<Stream<T>> valuesSupplier) {
        ValueSource<T> source = new ValueSource<>(type, valuesSupplier);
        SOURCES.add(source);
        return source;
    }

    public static List<ValueSource<?>> sources() {
        return Collections.unmodifiableList(SOURCES);
    }

    public static final ValueSource<Boolean> BOOLEAN = create(Type.BOOLEAN, () -> Stream.of(true, false));

    public static final ValueSource<Byte> BYTE = create(Type.BYTE, () -> Stream.of(Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE).map(Number::byteValue));

    public static final ValueSource<Character> CHAR = create(Type.CHAR, () -> Stream.of(Character.MIN_VALUE, 'a', '0', '!', 'あ', Character.MAX_VALUE));

    public static final ValueSource<Double> DOUBLE = create(Type.DOUBLE, () -> Stream.of(Double.MIN_VALUE, -Math.PI, -1.0, -0.0, 0.0, 1.0, Math.PI, Double.MAX_VALUE));

    public static final ValueSource<Float> FLOAT = create(Type.FLOAT, () -> Stream.of(Float.MIN_VALUE, (float) -Math.PI, -1f, -0.0f, 0.0f, 1f, (float) Math.PI, Float.MAX_VALUE));

    public static final ValueSource<Integer> INT = create(Type.INT, () -> Stream.of(Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE));

    public static final ValueSource<Long> LONG = create(Type.LONG, () -> Stream.of(Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE));

    public static final ValueSource<Short> SHORT = create(Type.SHORT, () -> Stream.of(Short.MIN_VALUE, -1, 0, 1, Short.MAX_VALUE).map(Number::shortValue));

    public static final ValueSource<String> STRING = create(Type.STRING, () -> Stream.of("a", "abc", " ", "あ", "1234", "!@#$%^&*()", "\n\r\n\t"));

    public Stream<T> values() {
        return this.valuesSupplier.get();
    }

    @Override
    public Codec<T> codec() {
        return Codec.byValueType(this.type);
    }
}
