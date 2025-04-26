package dev.siroshun.codec4j.api.io;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;

/**
 * An interface providing basic types that are supported by Codec4J.
 */
@NotNullByDefault
public sealed interface Type permits Type.ListType, Type.MapType, Type.Unknown, Type.Value {

    /**
     * A {@link Type} for {@link Boolean}.
     */
    BooleanValue BOOLEAN = DefinedTypes.defineType(new BooleanValue());

    /**
     * A {@link Type} for {@link Byte}.
     */
    ByteValue BYTE = DefinedTypes.defineType(new ByteValue());

    /**
     * A {@link Type} for {@link Character}.
     */
    CharValue CHAR = DefinedTypes.defineType(new CharValue());

    /**
     * A {@link Type} for {@link Double}.
     */
    DoubleValue DOUBLE = DefinedTypes.defineType(new DoubleValue());

    /**
     * A {@link Type} for {@link Float}.
     */
    FloatValue FLOAT = DefinedTypes.defineType(new FloatValue());

    /**
     * A {@link Type} for {@link Integer}.
     */
    IntValue INT = DefinedTypes.defineType(new IntValue());

    /**
     * A {@link Type} for {@link Long}.
     */
    LongValue LONG = DefinedTypes.defineType(new LongValue());

    /**
     * A {@link Type} for {@link Short}.
     */
    ShortValue SHORT = DefinedTypes.defineType(new ShortValue());

    /**
     * A {@link Type} for {@link String}.
     */
    StringValue STRING = DefinedTypes.defineType(new StringValue());

    /**
     * A {@link Type} for a list.
     */
    ListType LIST = DefinedTypes.defineType(new ListType());

    /**
     * A {@link Type} for a map.
     */
    MapType MAP = DefinedTypes.defineType(new MapType());

    /**
     * An unknown {@link Type}.
     */
    Unknown UNKNOWN = DefinedTypes.defineType(new Unknown());

    /**
     * Gets a {@link List} of all {@link Type}s.
     *
     * @return a {@link List} of all {@link Type}s
     */
    static List<Type> types() {
        return DefinedTypes.types();
    }

    /**
     * Gets a {@link List} of all known {@link Type}s.
     *
     * @return a {@link List} of all known {@link Type}s, excluding {@link Unknown} type
     */
    static List<Type> knownTypes() {
        return List.of(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT, STRING, LIST, MAP);
    }

    /**
     * Gets a {@link List} of all {@link Type.Value}s.
     *
     * @return a {@link List} of all {@link Type.Value}s
     */
    static List<Type.Value<?>> valueTypes() {
        return List.of(BOOLEAN, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT, STRING);
    }

    /**
     * Gets a {@link List} of all {@link Type.NumberValue}s.
     *
     * @return a {@link List} of all {@link Type.NumberValue}s
     */
    static List<Type.NumberValue<?>> numberTypes() {
        return List.of(BYTE, DOUBLE, FLOAT, INT, LONG, SHORT);
    }

    /**
     * Whether this {@link Type} is a {@link BooleanValue} type.
     *
     * @return {@code true} if this {@link Type} is a {@link BooleanValue} type, otherwise {@code false}
     */
    default boolean isBoolean() {
        return this instanceof BooleanValue;
    }

    /**
     * Whether this {@link Type} is a {@link NumberValue} type.
     *
     * @return {@code true} if this {@link Type} is a {@link NumberValue} type, otherwise {@code false}
     */
    default boolean isNumber() {
        return this instanceof NumberValue;
    }

    /**
     * Whether this {@link Type} is a {@link StringValue} type.
     *
     * @return {@code true} if this {@link Type} is a {@link StringValue} type, otherwise {@code false}
     */
    default boolean isString() {
        return this instanceof StringValue;
    }

    /**
     * Whether this {@link Type} is a {@link ListType} type.
     *
     * @return {@code true} if this {@link Type} is a {@link ListType} type, otherwise {@code false}
     */
    default boolean isList() {
        return this instanceof ListType;
    }

    /**
     * Whether this {@link Type} is a {@link MapType} type.
     *
     * @return {@code true} if this {@link Type} is a {@link MapType} type, otherwise {@code false}
     */
    default boolean isMap() {
        return this instanceof MapType;
    }

    /**
     * Whether this {@link Type} is an {@link Unknown} type.
     *
     * @return {@code true} if this {@link Type} is an {@link Unknown} type, otherwise {@code false}
     */
    default boolean isUnknown() {
        return this instanceof Unknown;
    }

    /**
     * An interface indicating that {@link Type} is {@link Value}.
     *
     * @param <T> the value type
     */
    @SuppressWarnings("unused")
    sealed interface Value<T> extends Type permits BooleanValue, CharValue, NumberValue, StringValue {
    }

    /**
     * A {@link Value} type for {@link Boolean}.
     */
    final class BooleanValue implements Value<Boolean> {
        private BooleanValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Character}.
     */
    final class CharValue implements Value<Character> {
        private CharValue() {
        }
    }

    /**
     * A {@link Value} type for {@link String}.
     */
    final class StringValue implements Value<String> {
        private StringValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Number}s.
     */
    sealed interface NumberValue<T> extends Value<T> {
    }

    /**
     * A {@link Value} type for {@link Byte}.
     */
    final class ByteValue implements NumberValue<Byte> {
        private ByteValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Double}.
     */
    final class DoubleValue implements NumberValue<Double> {
        private DoubleValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Float}.
     */
    final class FloatValue implements NumberValue<Float> {
        private FloatValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Integer}.
     */
    final class IntValue implements NumberValue<Integer> {
        private IntValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Long}.
     */
    final class LongValue implements NumberValue<Long> {
        private LongValue() {
        }
    }

    /**
     * A {@link Value} type for {@link Short}.
     */
    final class ShortValue implements NumberValue<Short> {
        private ShortValue() {
        }
    }

    /**
     * A {@link Type} for a list.
     */
    final class ListType implements Type {
        private ListType() {
        }
    }

    /**
     * A {@link Type} for a map.
     */
    final class MapType implements Type {
        private MapType() {
        }
    }

    /**
     * A {@link Type} for an unknown type.
     */
    final class Unknown implements Type {
        private Unknown() {
        }
    }
}
