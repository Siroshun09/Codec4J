package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;

/**
 * A {@link Codec} for {@link Enum}s.
 */
public final class EnumCodec {

    /**
     * Creates a new {@link Codec} of the specified {@link Enum} class using {@link Enum#name()} for encoding/decoding.
     *
     * @param enumClass the class of {@link E}
     * @return a new {@link Codec} of the specified {@link Enum} class
     * @param <E> a type of the {@link Enum}
     */
    public static <E extends Enum<E>> Codec<E> byName(Class<E> enumClass) {
        return Codec.STRING.flatXmap(
            e -> Result.success(e.name()),
            s -> {
                try {
                    E e = Enum.valueOf(enumClass, s);
                    return Result.success(e);
                } catch (IllegalArgumentException e) {
                    return new UnknownEnumNameDecodeError(enumClass, s).asFailure();
                }
            }
        );
    }

    /**
     * Creates a new {@link Codec} of the specified {@link Enum} class using {@link Enum#ordinal()} for encoding/decoding.
     *
     * @param enumClass the class of {@link E}
     * @return a new {@link Codec} of the specified {@link Enum} class
     * @param <E> a type of the {@link Enum}
     */
    public static <E extends Enum<E>> Codec<E> byOrdinal(Class<E> enumClass) {
        return Codec.INT.flatXmap(
            e -> Result.success(e.ordinal()),
            ordinal -> {
                E[] es = enumClass.getEnumConstants();
                if (ordinal < 0 || ordinal >= es.length) {
                    return new UnknownEnumOrdinalDecodeError(enumClass, ordinal).asFailure();
                }
                return Result.success(es[ordinal]);
            }
        );
    }

    public record UnknownEnumNameDecodeError(Class<?> enumClass, String name) implements DecodeError.Failure {
    }

    public record UnknownEnumOrdinalDecodeError(Class<?> enumClass, int ordinal) implements DecodeError.Failure {
    }

    private EnumCodec() {
        throw new UnsupportedOperationException();
    }
}
