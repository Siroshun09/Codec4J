package dev.siroshun.codec4j.api.decoder.object;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

record FieldList2<T, F1, F2>(FieldDecoder<F1> codec1, FieldDecoder<F2> codec2,
                             BiFunction<F1, F2, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public @NotNull ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.MultipleFieldObjectConstructor<T> {

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
