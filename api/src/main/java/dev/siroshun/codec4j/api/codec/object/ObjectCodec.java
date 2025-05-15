package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.function.Function10;
import dev.siroshun.jfun.function.Function3;
import dev.siroshun.jfun.function.Function4;
import dev.siroshun.jfun.function.Function5;
import dev.siroshun.jfun.function.Function6;
import dev.siroshun.jfun.function.Function7;
import dev.siroshun.jfun.function.Function8;
import dev.siroshun.jfun.function.Function9;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectCodec<T> implements Codec<T> {

    @Contract("_, _ -> new")
    public static <T, F1> @NotNull ObjectCodec<T> create(@NotNull Function<F1, T> constructor, @NotNull FieldCodec<T, F1> field1) {
        return new ObjectCodec<>(List.of(field1), new FieldList1<>(field1, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _ -> new")
    public static <T, F1, F2> @NotNull ObjectCodec<T> create(@NotNull BiFunction<F1, F2, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2) {
        return new ObjectCodec<>(List.of(field1, field2), new FieldList2<>(field1, field2, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _ -> new")
    public static <T, F1, F2, F3> @NotNull ObjectCodec<T> create(@NotNull Function3<F1, F2, F3, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3) {
        return new ObjectCodec<>(List.of(field1, field2, field3), new FieldList3<>(field1, field2, field3, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4> @NotNull ObjectCodec<T> create(@NotNull Function4<F1, F2, F3, F4, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4), new FieldList4<>(field1, field2, field3, field4, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5> @NotNull ObjectCodec<T> create(@NotNull Function5<F1, F2, F3, F4, F5, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5), new FieldList5<>(field1, field2, field3, field4, field5, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6> @NotNull ObjectCodec<T> create(@NotNull Function6<F1, F2, F3, F4, F5, F6, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5, field6), new FieldList6<>(field1, field2, field3, field4, field5, field6, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7> @NotNull ObjectCodec<T> create(@NotNull Function7<F1, F2, F3, F4, F5, F6, F7, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5, field6, field7), new FieldList7<>(field1, field2, field3, field4, field5, field6, field7, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8> @NotNull ObjectCodec<T> create(@NotNull Function8<F1, F2, F3, F4, F5, F6, F7, F8, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8), new FieldList8<>(field1, field2, field3, field4, field5, field6, field7, field8, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9> @NotNull ObjectCodec<T> create(@NotNull Function9<F1, F2, F3, F4, F5, F6, F7, F8, F9, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8, @NotNull FieldCodec<T, F9> field9) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8, field9), new FieldList9<>(field1, field2, field3, field4, field5, field6, field7, field8, field9, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> @NotNull ObjectCodec<T> create(@NotNull Function10<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> constructor, @NotNull FieldCodec<T, F1> field1, @NotNull FieldCodec<T, F2> field2, @NotNull FieldCodec<T, F3> field3, @NotNull FieldCodec<T, F4> field4, @NotNull FieldCodec<T, F5> field5, @NotNull FieldCodec<T, F6> field6, @NotNull FieldCodec<T, F7> field7, @NotNull FieldCodec<T, F8> field8, @NotNull FieldCodec<T, F9> field9, @NotNull FieldCodec<T, F10> field10) {
        return new ObjectCodec<>(List.of(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10), new FieldList10<>(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, Objects.requireNonNull(constructor)));
    }

    private final List<FieldCodec<T, ?>> fieldList;
    private final Supplier<ObjectConstructor<T>> objectConstructorSupplier;

    ObjectCodec(@NotNull List<FieldCodec<T, ?>> fieldList,
                @NotNull Supplier<ObjectConstructor<T>> objectConstructorSupplier) {
        this.fieldList = fieldList;
        this.objectConstructorSupplier = objectConstructorSupplier;
    }

    @Override
    public <O> @NotNull Result<O, EncodeError> encode(@NotNull Out<O> out, @UnknownNullability T input) {
        Result<EntryAppender<O>, EncodeError> result = out.createMap();

        if (result.isFailure()) {
            return result.asFailure();
        }

        var appender = result.unwrap();

        for (var field : this.fieldList) {
            if (field.canOmit(input)) {
                continue;
            }
            var encodeResult = appender.append(keyOut -> keyOut.writeString(field.fieldName()), valueOut -> field.encodeFieldValue(valueOut, input));
            if (encodeResult.isFailure()) {
                return result.asFailure();
            }
        }

        return appender.finish();
    }

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return in.readMap(
                this.objectConstructorSupplier.get(),
                (constructor, entryIn) -> {
                    var type = entryIn.keyIn().type();

                    if (type.isFailure()) {
                        return type.asFailure();
                    }

                    if (!type.unwrap().isString()) {
                        return DecodeError.typeMismatch(Type.STRING, type.unwrap()).asFailure();
                    }

                    var fieldNameResult = entryIn.keyIn().readAsString();

                    if (fieldNameResult.isFailure()) {
                        return fieldNameResult.asFailure();
                    }

                    return constructor.decodeField(fieldNameResult.unwrap(), entryIn.valueIn());
                }).flatMap(ObjectConstructor::construct);
    }

    interface ObjectConstructor<T> {

        @NotNull Result<Void, DecodeError> decodeField(String fieldName, In in);

        @NotNull Result<T, DecodeError> construct();

    }

    interface MultipleFieldObjectConstructor<T> extends ObjectConstructor<T> {

        default @NotNull Result<Void, DecodeError> decodeField(String fieldName, In in) {
            var field = this.fieldMap().get(fieldName);

            if (field != null) {
                var result = field.decode(in);
                if (result.isFailure()) {
                    return result.asFailure();
                }
            }

            return Result.success();
        }

        @Override
        default @NotNull Result<T, DecodeError> construct() {
            for (var field : this.fieldMap().values()) {
                Result<Void, DecodeError> result = field.checkDecoded();
                if (result.isFailure()) {
                    return result.asFailure();
                }
            }
            return Result.success(this.constructObject());
        }

        @NotNull Map<String, FieldValue<?>> fieldMap();

        @NotNull T constructObject();
    }
}
