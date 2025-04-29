package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.jfun.function.Function9;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

record FieldList9<T, F1, F2, F3, F4, F5, F6, F7, F8, F9>(FieldCodec<T, F1> codec1, FieldCodec<T, F2> codec2, FieldCodec<T, F3> codec3, FieldCodec<T, F4> codec4, FieldCodec<T, F5> codec5, FieldCodec<T, F6> codec6, FieldCodec<T, F7> codec7, FieldCodec<T, F8> codec8, FieldCodec<T, F9> codec9, Function9<F1, F2, F3, F4, F5, F6, F7, F8, F9, T> constructor) implements Supplier<ObjectCodec.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectCodec.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectCodec.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList9.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList9.this.codec2);

        private final FieldValue<F3> field3 = new FieldValue<>(FieldList9.this.codec3);

        private final FieldValue<F4> field4 = new FieldValue<>(FieldList9.this.codec4);

        private final FieldValue<F5> field5 = new FieldValue<>(FieldList9.this.codec5);

        private final FieldValue<F6> field6 = new FieldValue<>(FieldList9.this.codec6);

        private final FieldValue<F7> field7 = new FieldValue<>(FieldList9.this.codec7);

        private final FieldValue<F8> field8 = new FieldValue<>(FieldList9.this.codec8);

        private final FieldValue<F9> field9 = new FieldValue<>(FieldList9.this.codec9);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList9.this.codec1.fieldName(), this.field1, FieldList9.this.codec2.fieldName(), this.field2, FieldList9.this.codec3.fieldName(), this.field3, FieldList9.this.codec4.fieldName(), this.field4, FieldList9.this.codec5.fieldName(), this.field5, FieldList9.this.codec6.fieldName(), this.field6, FieldList9.this.codec7.fieldName(), this.field7, FieldList9.this.codec8.fieldName(), this.field8, FieldList9.this.codec9.fieldName(), this.field9);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList9.this.constructor.apply(this.field1.result(), this.field2.result(), this.field3.result(), this.field4.result(), this.field5.result(), this.field6.result(), this.field7.result(), this.field8.result(), this.field9.result());
        }

    }

}
