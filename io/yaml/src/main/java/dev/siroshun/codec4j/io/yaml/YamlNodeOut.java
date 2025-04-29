package dev.siroshun.codec4j.io.yaml;

import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.ElementAppender;
import dev.siroshun.codec4j.api.io.EntryAppender;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNullByDefault;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotNullByDefault
class YamlNodeOut implements Out<Node> {

    private final YamlParameter parameter;
    private final Representer representer;

    YamlNodeOut(YamlParameter parameter, Representer representer) {
        this.parameter = parameter;
        this.representer = representer;
    }

    @Override
    public Result<Node, EncodeError> writeBoolean(boolean value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeByte(byte value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeChar(char value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeDouble(double value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeFloat(float value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeInt(int value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeLong(long value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeShort(short value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<Node, EncodeError> writeString(String value) {
        return Result.success(this.representer.represent(value));
    }

    @Override
    public Result<ElementAppender<Node>, EncodeError> createList() {
        return Result.success(new NodeElementAppender(new ArrayList<>(), this.parameter, this.representer));
    }

    @Override
    public Result<EntryAppender<Node>, EncodeError> createMap() {
        return Result.success(new NodeEntryAppender(new ArrayList<>(), this.parameter, this.representer));
    }

    private record NodeElementAppender(List<Node> nodes,
                                       YamlParameter parameter,
                                       Representer representer) implements ElementAppender<Node> {
        @Override
        public Result<Node, EncodeError> append(Function<Out<Node>, Result<Node, EncodeError>> function) {
            var result = function.apply(new YamlNodeOut(this.parameter, this.representer));
            if (result.isFailure()) {
                return result.asFailure();
            }
            this.nodes.add(result.unwrap());
            return result;
        }

        @Override
        public Result<Node, EncodeError> finish() {
            return Result.success(new SequenceNode(Tag.SEQ, this.nodes, this.parameter.sequenceFlowStyle()));
        }
    }

    private record NodeEntryAppender(List<NodeTuple> nodes,
                                     YamlParameter parameter,
                                     Representer representer) implements EntryAppender<Node> {

        @Override
        public Result<Void, EncodeError> append(Function<Out<Node>, Result<Node, EncodeError>> keyWriter, Function<Out<Node>, Result<Node, EncodeError>> valueWriter) {
            var keyResult = keyWriter.apply(new YamlNodeOut(this.parameter, this.representer));
            if (keyResult.isFailure()) {
                return keyResult.asFailure();
            }

            var valueResult = valueWriter.apply(new YamlNodeOut(this.parameter, this.representer));
            if (valueResult.isFailure()) {
                return valueResult.asFailure();
            }

            this.nodes.add(new NodeTuple(keyResult.unwrap(), valueResult.unwrap()));
            return Result.success();
        }

        @Override
        public Result<Node, EncodeError> finish() {
            return Result.success(new MappingNode(Tag.MAP, this.nodes, this.parameter.mapFlowStyle()));
        }
    }
}
