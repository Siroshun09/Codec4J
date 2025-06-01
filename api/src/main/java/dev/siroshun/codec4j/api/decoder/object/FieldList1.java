package dev.siroshun.codec4j.api.decoder.object;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

record FieldList1<T, F1>(FieldDecoder<F1> codec,
                         Function<@Nullable F1, T> constructor) implements Supplier<ObjectDecoder.ObjectConstructor<T>> {

    @Override
    public ObjectDecoder.ObjectConstructor<T> get() {
        return new Constructor();
    }

    private class Constructor implements ObjectDecoder.ObjectConstructor<T> {

        private @Nullable F1 decodedField = null;
        private boolean decoded = false;

        private Constructor() {
        }

        @Override
        public @NotNull Result<Void, DecodeError> decodeField(String fieldName, In in) {
            FieldDecoder<F1> field = FieldList1.this.codec;
            if (field.fieldName().equals(fieldName)) {
                Result<F1, DecodeError> result = field.decodeFieldValue(in);
                if (result.isSuccess()) {
                    this.decodedField = result.unwrap();
                    this.decoded = true;
                } else if (result.unwrapError() instanceof DecodeError.IgnorableError) {
                    return Result.success();
                } else {
                    return result.asFailure();
                }
            }
            return Result.success();
        }

        @Override
        public @NotNull Result<T, DecodeError> construct() {
            return this.decoded ?
                    Result.success(FieldList1.this.constructor.apply(this.decodedField)) :
                    FieldList1.this.codec.onNotDecoded().map(FieldList1.this.constructor);
        }
    }
}

