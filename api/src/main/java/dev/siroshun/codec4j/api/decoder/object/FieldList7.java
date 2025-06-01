package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.jfun.function.Function7;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

record FieldList7<T, F1, F2, F3, F4, F5, F6, F7>(FieldDecoder<F1> codec1, FieldDecoder<F2> codec2, FieldDecoder<F3> codec3, FieldDecoder<F4> codec4, FieldDecoder<F5> codec5, FieldDecoder<F6> codec6, FieldDecoder<F7> codec7, Function7<F1, F2, F3, F4, F5, F6, F7, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Contract(" -> new")
    @Override
    public  ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.MultipleFieldObjectConstructor<T> {

        private final FieldValue<F1> field1 = new FieldValue<>(FieldList7.this.codec1);

        private final FieldValue<F2> field2 = new FieldValue<>(FieldList7.this.codec2);

        private final FieldValue<F3> field3 = new FieldValue<>(FieldList7.this.codec3);

        private final FieldValue<F4> field4 = new FieldValue<>(FieldList7.this.codec4);

        private final FieldValue<F5> field5 = new FieldValue<>(FieldList7.this.codec5);

        private final FieldValue<F6> field6 = new FieldValue<>(FieldList7.this.codec6);

        private final FieldValue<F7> field7 = new FieldValue<>(FieldList7.this.codec7);

        private final Map<String, FieldValue<?>> fieldMap = Map.of(FieldList7.this.codec1.fieldName(), this.field1, FieldList7.this.codec2.fieldName(), this.field2, FieldList7.this.codec3.fieldName(), this.field3, FieldList7.this.codec4.fieldName(), this.field4, FieldList7.this.codec5.fieldName(), this.field5, FieldList7.this.codec6.fieldName(), this.field6, FieldList7.this.codec7.fieldName(), this.field7);

        @Override
        public @NotNull Map<String, FieldValue<?>> fieldMap() {
            return this.fieldMap;
        }

        @Override
        public @NotNull T constructObject() {
            return FieldList7.this.constructor.apply(this.field1.result(), this.field2.result(), this.field3.result(), this.field4.result(), this.field5.result(), this.field6.result(), this.field7.result());
        }

    }

}
