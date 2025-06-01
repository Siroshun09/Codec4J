package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.jfun.function.Function5;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

record FieldList5<T, F1, F2, F3, F4, F5>(FieldDecoder<F1> codec1, FieldDecoder<F2> codec2, FieldDecoder<F3> codec3, FieldDecoder<F4> codec4, FieldDecoder<F5> codec5, Function5<F1, F2, F3, F4, F5, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList5.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList5.this.codec2);

        private final FieldValue<F3> field3 = new FieldValue<>(FieldList5.this.codec3);

        private final FieldValue<F4> field4 = new FieldValue<>(FieldList5.this.codec4);

        private final FieldValue<F5> field5 = new FieldValue<>(FieldList5.this.codec5);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList5.this.codec1.fieldName(), this.field1, FieldList5.this.codec2.fieldName(), this.field2, FieldList5.this.codec3.fieldName(), this.field3, FieldList5.this.codec4.fieldName(), this.field4, FieldList5.this.codec5.fieldName(), this.field5);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList5.this.constructor.apply(this.field1.result(), this.field2.result(), this.field3.result(), this.field4.result(), this.field5.result());
        }

    }

}
