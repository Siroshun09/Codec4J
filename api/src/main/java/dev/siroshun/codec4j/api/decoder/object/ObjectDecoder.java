package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
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

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ObjectDecoder<T> implements Decoder<T> {

    @Contract("_, _ -> new")
    public static <T, F1> @NotNull ObjectDecoder<T> create(@NotNull Function<F1, T> constructor, @NotNull FieldDecoder<F1> field1) {
        return new ObjectDecoder<>(new FieldList1<>(field1, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _ -> new")
    public static <T, F1, F2> @NotNull ObjectDecoder<T> create(@NotNull BiFunction<F1, F2, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2) {
        return new ObjectDecoder<>(new FieldList2<>(field1, field2, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _ -> new")
    public static <T, F1, F2, F3> @NotNull ObjectDecoder<T> create(@NotNull Function3<F1, F2, F3, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3) {
        return new ObjectDecoder<>(new FieldList3<>(field1, field2, field3, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4> @NotNull ObjectDecoder<T> create(@NotNull Function4<F1, F2, F3, F4, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4) {
        return new ObjectDecoder<>(new FieldList4<>(field1, field2, field3, field4, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5> @NotNull ObjectDecoder<T> create(@NotNull Function5<F1, F2, F3, F4, F5, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5) {
        return new ObjectDecoder<>(new FieldList5<>(field1, field2, field3, field4, field5, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6> @NotNull ObjectDecoder<T> create(@NotNull Function6<F1, F2, F3, F4, F5, F6, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5, @NotNull FieldDecoder<F6> field6) {
        return new ObjectDecoder<>(new FieldList6<>(field1, field2, field3, field4, field5, field6, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7> @NotNull ObjectDecoder<T> create(@NotNull Function7<F1, F2, F3, F4, F5, F6, F7, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5, @NotNull FieldDecoder<F6> field6, @NotNull FieldDecoder<F7> field7) {
        return new ObjectDecoder<>(new FieldList7<>(field1, field2, field3, field4, field5, field6, field7, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8> @NotNull ObjectDecoder<T> create(@NotNull Function8<F1, F2, F3, F4, F5, F6, F7, F8, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5, @NotNull FieldDecoder<F6> field6, @NotNull FieldDecoder<F7> field7, @NotNull FieldDecoder<F8> field8) {
        return new ObjectDecoder<>(new FieldList8<>(field1, field2, field3, field4, field5, field6, field7, field8, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9> @NotNull ObjectDecoder<T> create(@NotNull Function9<F1, F2, F3, F4, F5, F6, F7, F8, F9, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5, @NotNull FieldDecoder<F6> field6, @NotNull FieldDecoder<F7> field7, @NotNull FieldDecoder<F8> field8, @NotNull FieldDecoder<F9> field9) {
        return new ObjectDecoder<>(new FieldList9<>(field1, field2, field3, field4, field5, field6, field7, field8, field9, Objects.requireNonNull(constructor)));
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <T, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10> @NotNull ObjectDecoder<T> create(@NotNull Function10<F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, T> constructor, @NotNull FieldDecoder<F1> field1, @NotNull FieldDecoder<F2> field2, @NotNull FieldDecoder<F3> field3, @NotNull FieldDecoder<F4> field4, @NotNull FieldDecoder<F5> field5, @NotNull FieldDecoder<F6> field6, @NotNull FieldDecoder<F7> field7, @NotNull FieldDecoder<F8> field8, @NotNull FieldDecoder<F9> field9, @NotNull FieldDecoder<F10> field10) {
        return new ObjectDecoder<>(new FieldList10<>(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, Objects.requireNonNull(constructor)));
    }

    private final Supplier<ObjectConstructor<T>> objectConstructorSupplier;

    ObjectDecoder(@NotNull Supplier<ObjectConstructor<T>> objectConstructorSupplier) {
        this.objectConstructorSupplier = objectConstructorSupplier;
    }

    @Override
    public @NotNull Result<T, DecodeError> decode(@NotNull In in) {
        return in.readMap(
            this.objectConstructorSupplier.get(),
            (constructor, entryIn) -> {
                Result<String, DecodeError> fieldNameResult = entryIn.keyIn().readAsString();

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
            FieldValue<?> field = this.fieldMap().get(fieldName);

            if (field != null) {
                Result<Void, DecodeError> result = field.decode(in);
                if (result.isFailure()) {
                    return result.asFailure();
                }
            }

            return Result.success();
        }

        @Override
        default @NotNull Result<T, DecodeError> construct() {
            for (FieldValue<?> field : this.fieldMap().values()) {
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
