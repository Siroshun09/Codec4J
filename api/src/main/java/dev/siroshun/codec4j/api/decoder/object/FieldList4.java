package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.jfun.function.Function4;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

record FieldList4<T, F1, F2, F3, F4>(FieldDecoder<F1> codec1, FieldDecoder<F2> codec2, FieldDecoder<F3> codec3, FieldDecoder<F4> codec4, Function4<F1, F2, F3, F4, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList4.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList4.this.codec2);

        private final FieldValue<F3> field3 = new FieldValue<>(FieldList4.this.codec3);

        private final FieldValue<F4> field4 = new FieldValue<>(FieldList4.this.codec4);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList4.this.codec1.fieldName(), this.field1, FieldList4.this.codec2.fieldName(), this.field2, FieldList4.this.codec3.fieldName(), this.field3, FieldList4.this.codec4.fieldName(), this.field4);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList4.this.constructor.apply(this.field1.result(), this.field2.result(), this.field3.result(), this.field4.result());
        }

    }

}
