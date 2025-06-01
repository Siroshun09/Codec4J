package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.jfun.function.Function3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

record FieldList3<T, F1, F2, F3>(FieldDecoder<F1> codec1, FieldDecoder<F2> codec2, FieldDecoder<F3> codec3, Function3<F1, F2, F3, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.MultipleFieldObjectConstructor<T> {

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
