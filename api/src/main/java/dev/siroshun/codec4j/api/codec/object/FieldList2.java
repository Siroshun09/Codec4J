package dev.siroshun.codec4j.api.codec.object;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

record FieldList2<T, F1, F2>(FieldCodec<T, F1> codec1, FieldCodec<T, F2> codec2, BiFunction<F1, F2, T> constructor) implements Supplier<ObjectCodec.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public @NotNull ObjectCodec.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectCodec.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList2.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList2.this.codec2);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList2.this.codec1.fieldName(), this.field1, FieldList2.this.codec2.fieldName(), this.field2);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList2.this.constructor.apply(this.field1.result(), this.field2.result());
        }

    }

}
