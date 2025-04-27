package dev.siroshun.codec4j.api.codec;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.function.Function;

/**
 * An interface for encoding and decoding data.
 *
 * @param <T> the type of the data
 */
public interface Codec<T> extends Encoder<T>, Decoder<T> {

    /**
     * Creates a new {@link Codec} with the specified {@link Encoder} and {@link Decoder}.
     *
     * @param encoder the {@link Encoder} for encoding the data
     * @param decoder the {@link Decoder} for decoding the data
     * @param <A>     the type of the data
     * @return a new {@link Codec} with the specified {@link Encoder} and {@link Decoder}
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <A> @NotNull Codec<A> codec(@NotNull Encoder<? super A> encoder, @NotNull Decoder<? extends A> decoder) {
        Objects.requireNonNull(encoder);
        Objects.requireNonNull(decoder);
        return new SimpleCodec<>(encoder, decoder);
    }

    /**
     * Creates a new {@link Codec} with the specified {@link Encoder}, {@link Decoder} and name.
     *
     * @param encoder the {@link Encoder} for encoding the data
     * @param decoder the {@link Decoder} for decoding the data
     * @param name    the name of the new {@link Codec}
     * @param <A>     the type of the data
     * @return a new {@link Codec} with the specified {@link Encoder}, {@link Decoder} and name
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static <A> @NotNull Codec<A> codec(@NotNull Encoder<? super A> encoder, @NotNull Decoder<? extends A> decoder, @NotNull String name) {
        Objects.requireNonNull(name);
        return new NamedCodec<>(name, codec(encoder, decoder));
    }

    /**
     * Maps the data from the provided {@link T} to another type.
     *
     * @param fromA the {@link Function} to map the data from the provided {@link T} to another type
     * @param toA   the {@link Function} to map the data from another type to the provided {@link T}
     * @param <A>   the type of the mapped data
     * @return a new {@link Codec} that maps the data from the provided {@link T} to another type
     */
    <A> @NotNull Codec<A> xmap(@NotNull Function<? super A, ? extends T> fromA, @NotNull Function<? super T, ? extends A> toA);

    /**
     * Maps the data from the provided {@link T} to another type and flattens the {@link Result}.
     *
     * @param fromA the {@link Function} to map the data from the provided {@link T} to another type and returns a {@link Result} of the mapping
     * @param toA   the {@link Function} to map the data from another type to the provided {@link T} and returns a {@link Result} of the mapping
     * @param <A>   the type of the mapped data
     * @return a new {@link Codec} that maps the data from the provided {@link T} to another type and flattens the {@link Result}
     */
    <A> @NotNull Codec<A> flatXmap(@NotNull Function<? super A, Result<T, EncodeError>> fromA, @NotNull Function<? super T, Result<A, DecodeError>> toA);

    /**
     * Creates a new {@link Codec} with the specified name and this {@link Codec} as the delegate codec.
     *
     * @param name the name of the new {@link Codec}
     * @return a named {@link Codec} with this {@link Codec} as the delegate codec
     */
    @NotNull Codec<T> named(@NotNull String name);

    /**
     * A {@link Codec} for {@link Boolean}.
     * <p>
     * This {@link Codec} calls {@link Out#writeBoolean(boolean)} for encoding, and {@link In#readAsBoolean()} for decoding.
     */
    Codec<Boolean> BOOLEAN = Codec.codec(Out::writeBoolean, In::readAsBoolean, "Boolean");

    /**
     * A {@link Codec} for {@link Byte}.
     * <p>
     * This {@link Codec} calls {@link Out#writeByte(byte)} for encoding, and {@link In#readAsByte()} for decoding.
     */
    Codec<Byte> BYTE = Codec.codec(Out::writeByte, In::readAsByte, "Byte");

    /**
     * A {@link Codec} for {@link Character}.
     * <p>
     * This {@link Codec} calls {@link Out#writeByte(byte)} for encoding, and {@link In#readAsChar()} for decoding.
     */
    Codec<Character> CHAR = Codec.codec(Out::writeChar, In::readAsChar, "Char");

    /**
     * A {@link Codec} for {@link Double}.
     * <p>
     * This {@link Codec} calls {@link Out#writeDouble(double)} for encoding, and {@link In#readAsDouble()} for decoding.
     */
    Codec<Double> DOUBLE = Codec.codec(Out::writeDouble, In::readAsDouble, "Double");

    /**
     * A {@link Codec} for {@link Float}.
     * <p>
     * This {@link Codec} calls {@link Out#writeFloat(float)} for encoding, and {@link In#readAsFloat()} for decoding.
     */
    Codec<Float> FLOAT = Codec.codec(Out::writeFloat, In::readAsFloat, "Float");

    /**
     * A {@link Codec} for {@link Integer}.
     * <p>
     * This {@link Codec} calls {@link Out#writeInt(int)} for encoding, and {@link In#readAsInt()} for decoding.
     */
    Codec<Integer> INT = Codec.codec(Out::writeInt, In::readAsInt, "Int");

    /**
     * A {@link Codec} for {@link Long}.
     * <p>
     * This {@link Codec} calls {@link Out#writeLong(long)} for encoding, and {@link In#readAsLong()} for decoding.
     */
    Codec<Long> LONG = Codec.codec(Out::writeLong, In::readAsLong, "Long");

    /**
     * A {@link Codec} for {@link Short}.
     * <p>
     * This {@link Codec} calls {@link Out#writeShort(short)} for encoding, and {@link In#readAsShort()} for decoding.
     */
    Codec<Short> SHORT = Codec.codec(Out::writeShort, In::readAsShort, "Short");

    /**
     * A {@link Codec} for {@link String}.
     * <p>
     * This {@link Codec} calls {@link Out#writeString(String)} for encoding, and {@link In#readAsString()} for decoding.
     */
    Codec<String> STRING = Codec.codec(Out::writeString, In::readAsString, "String");

    /**
     * Gets the {@link Codec} of the specified {@link Type.Value}.
     *
     * @param type the {@link Type.Value} to get {@link Codec}
     * @param <T>  the type of the value
     * @return the {@link Codec} of the specified {@link Type.Value}
     */
    @SuppressWarnings("unchecked")
    static <T> @NotNull Codec<T> byValueType(@NotNull Type.Value<T> type) {
        Objects.requireNonNull(type);
        return (Codec<T>) switch (type) {
            case Type.BooleanValue ignored -> BOOLEAN;
            case Type.ByteValue ignored -> BYTE;
            case Type.CharValue ignored -> CHAR;
            case Type.DoubleValue ignored -> DOUBLE;
            case Type.FloatValue ignored -> FLOAT;
            case Type.IntValue ignored -> INT;
            case Type.LongValue ignored -> LONG;
            case Type.ShortValue ignored -> SHORT;
            case Type.StringValue ignored -> STRING;
        };
    }

    /**
     * An interface for delegating {@link Codec}.
     *
     * @param <T> the type of the data
     */
    interface Delegated<T> extends Codec<T> {

        /**
         * Returns the delegated {@link Codec}.
         *
         * @return the delegated {@link Codec}
         */
        @NotNull Codec<T> codec();

        @Override
        default @NotNull <O> Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
            return this.codec().encode(out, input);
        }

        @Override
        default @NotNull Result<T, DecodeError> decode(@NotNull In in) {
            return this.codec().decode(in);
        }

        @Override
        default <A> @NotNull Codec<A> xmap(@NotNull Function<? super A, ? extends T> fromA, @NotNull Function<? super T, ? extends A> toA) {
            return this.codec().xmap(fromA, toA);
        }

        @Override
        default <A> @NotNull Codec<A> flatXmap(@NotNull Function<? super A, Result<T, EncodeError>> fromA, @NotNull Function<? super T, Result<A, DecodeError>> toA) {
            return this.codec().flatXmap(fromA, toA);
        }

        @Override
        default @NotNull Codec<T> named(@NotNull String name) {
            return this.codec().named(name);
        }
    }
}
