package dev.siroshun.codec4j.io.yaml;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.EntryIn;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Type;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

import java.util.List;
import java.util.function.BiFunction;

@NotNullByDefault
class YamlNodeIn implements In {

    private final Node node;
    private final ObjectConstructor constructor;
    private @Nullable Object constructedObject;

    YamlNodeIn(Node node, ObjectConstructor constructor) {
        this.node = node instanceof AnchorNode anchorNode ? anchorNode.getRealNode() : node;
        this.constructor = constructor;
    }

    @Override
    public Result<Type, DecodeError> type() {
        return Result.success(this.getType());
    }

    private Type getType() {
        return switch (this.node) {
            case MappingNode ignored -> Type.MAP;
            case SequenceNode ignored -> Type.LIST;
            case ScalarNode ignored -> {
                Object object = this.constructObject();
                yield switch (object) {
                    case String ignored2 -> Type.STRING;
                    case Boolean ignored2 -> Type.BOOLEAN;
                    case Integer ignored2 -> Type.INT;
                    case Long ignored2 -> Type.LONG;
                    case Double ignored2 -> Type.DOUBLE;
                    case Float ignored2 -> Type.FLOAT;
                    case Byte ignored2 -> Type.BYTE;
                    case Short ignored2 -> Type.SHORT;
                    case null, default -> Type.UNKNOWN;
                };
            }
            default -> Type.UNKNOWN;
        };
    }

    @Override
    public Result<Boolean, DecodeError> readAsBoolean() {
        if (this.constructObject() instanceof Boolean bool) {
            return Result.success(bool);
        } else {
            return DecodeError.typeMismatch(Type.BOOLEAN, this.getType()).asFailure();
        }
    }

    @Override
    public Result<Byte, DecodeError> readAsByte() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            long value = n.longValue();
            return Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE ?
                Result.success((byte) value) :
                DecodeError.invalidNumber(Type.BYTE, value).asFailure();
        }
        return DecodeError.typeMismatch(Type.BYTE, this.getType()).asFailure();
    }

    @Override
    public Result<Character, DecodeError> readAsChar() {
        Object object = this.constructObject();
        if (object instanceof Character c) {
            return Result.success(c);
        } else if (object instanceof String s) {
            return s.length() == 1 ?
                Result.success(s.charAt(0)) :
                DecodeError.invalidChar(s).asFailure();
        }
        return DecodeError.typeMismatch(Type.CHAR, this.getType()).asFailure();
    }

    @Override
    public Result<Double, DecodeError> readAsDouble() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            return Result.success(n.doubleValue());
        }
        return DecodeError.typeMismatch(Type.DOUBLE, this.getType()).asFailure();
    }

    @Override
    public Result<Float, DecodeError> readAsFloat() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            double value = n.doubleValue();
            return Float.MIN_VALUE <= value && value <= Float.MAX_VALUE ?
                Result.success((float) value) :
                DecodeError.invalidNumber(Type.FLOAT, value).asFailure();
        }
        return DecodeError.typeMismatch(Type.FLOAT, this.getType()).asFailure();
    }

    @Override
    public Result<Integer, DecodeError> readAsInt() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            long value = n.longValue();
            return Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE ?
                Result.success((int) value) :
                DecodeError.invalidNumber(Type.INT, value).asFailure();
        }
        return DecodeError.typeMismatch(Type.INT, this.getType()).asFailure();
    }

    @Override
    public Result<Long, DecodeError> readAsLong() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            return Result.success(n.longValue());
        }
        return DecodeError.typeMismatch(Type.LONG, this.getType()).asFailure();
    }

    @Override
    public Result<Short, DecodeError> readAsShort() {
        Object object = this.constructObject();
        if (object instanceof Number n) {
            long value = n.longValue();
            return Short.MIN_VALUE <= value && value <= Short.MAX_VALUE ?
                Result.success((short) value) :
                DecodeError.invalidNumber(Type.SHORT, value).asFailure();
        }
        return DecodeError.typeMismatch(Type.SHORT, this.getType()).asFailure();
    }

    @Override
    public Result<String, DecodeError> readAsString() {
        Object object = this.constructObject();
        if (object instanceof String str) {
            return Result.success(str);
        } else if (this.node instanceof ScalarNode scalarNode) {
            return Result.success(scalarNode.getValue());
        }
        return DecodeError.typeMismatch(Type.STRING, this.getType()).asFailure();
    }

    @Override
    public <R> Result<R, DecodeError> readList(R identity, BiFunction<R, ? super In, Result<?, ?>> operator) {
        if (!(this.node instanceof SequenceNode sequenceNode)) {
            return DecodeError.typeMismatch(Type.LIST, this.getType()).asFailure();
        }

        List<Node> nodes = sequenceNode.getValue();

        for (Node node : nodes) {
            Result<?, ?> result = operator.apply(identity, new YamlNodeIn(node, this.constructor));
            if (result.isFailure()) {
                return DecodeError.iterationError(result.asFailure()).asFailure();
            }
        }

        return Result.success(identity);
    }

    @Override
    public <R> Result<R, DecodeError> readMap(R identity, BiFunction<R, ? super EntryIn, Result<?, ?>> operator) {
        if (!(this.node instanceof MappingNode mappingNode)) {
            return DecodeError.typeMismatch(Type.MAP, this.getType()).asFailure();
        }

        List<NodeTuple> tuples = mappingNode.getValue();

        for (NodeTuple tuple : tuples) {
            Result<?, ?> result = operator.apply(identity, new YamlTupleIn(tuple, this.constructor));
            if (result.isFailure()) {
                return DecodeError.iterationError(result.asFailure()).asFailure();
            }
        }

        return Result.success(identity);
    }

    private @Nullable Object constructObject() {
        if (this.constructedObject == null) {
            this.constructedObject = this.constructor.constructObject(this.node);
        }
        return this.constructedObject;
    }

    private record YamlTupleIn(NodeTuple tuple, ObjectConstructor constructor) implements EntryIn {
        @Override
        public In keyIn() {
            return new YamlNodeIn(this.tuple.getKeyNode(), this.constructor);
        }

        @Override
        public In valueIn() {
            return new YamlNodeIn(this.tuple.getValueNode(), this.constructor);
        }
    }
}
