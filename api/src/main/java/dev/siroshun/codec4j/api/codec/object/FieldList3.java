package dev.siroshun.codec4j.api.codec.object;

import dev.siroshun.jfun.function.Function3;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

record FieldList3<T, F1, F2, F3>(FieldCodec<T, F1> codec1, FieldCodec<T, F2> codec2, FieldCodec<T, F3> codec3, Function3<F1, F2, F3, T> constructor) implements Supplier<ObjectCodec.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectCodec.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectCodec.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList3.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList3.this.codec2);

        private final FieldValue<F3> field3 = new FieldValue<>(FieldList3.this.codec3);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList3.this.codec1.fieldName(), this.field1, FieldList3.this.codec2.fieldName(), this.field2, FieldList3.this.codec3.fieldName(), this.field3);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList3.this.constructor.apply(this.field1.result(), this.field2.result(), this.field3.result());
        }

    }

}
