package dev.siroshun.codec4j.io.yaml;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.encoder.Encoder;
import dev.siroshun.codec4j.api.file.TextFileIO;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.jfun.result.Result;

import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.UnknownNullability;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

import java.io.Reader;
import java.io.Writer;

/**
 * An implementation of {@link TextFileIO} for YAML.
 */
@NotNullByDefault
public final class YamlIO implements TextFileIO {

    /**
     * The default {@link YamlIO}.
     */
    public static final YamlIO DEFAULT = new YamlIO(new YamlParameter(
            DumperOptions.FlowStyle.BLOCK,
            DumperOptions.FlowStyle.BLOCK,
            DumperOptions.FlowStyle.BLOCK,
            DumperOptions.ScalarStyle.PLAIN,
            2
    ));

    private final YamlParameter parameter;
    private final YamlNodeOut out;
    private final ThreadLocal<Yaml> yamlHolder;

    private YamlIO(YamlParameter parameter) {
        this.parameter = parameter;
        this.out = new YamlNodeOut(parameter, parameter.createRepresenter());
        this.yamlHolder = ThreadLocal.withInitial(parameter::createYaml);
    }

    /**
     * Returns the {@link Out}, that creating snakeyaml's {@link Node}s.
     *
     * @return the {@link Out}
     */
    public Out<Node> nodeOut() {
        return this.out;
    }

    /**
     * Creates a new {@link In} from snakeyaml's {@link Node}.
     *
     * @param node the {@link Node} to create {@link In} from
     * @return the {@link In}
     */
    public In nodeIn(Node node) {
        return new YamlNodeIn(node, this.parameter.createConstructor());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Result<T, DecodeError> decodeFrom(Reader reader, Decoder<? extends T> decoder) {
        Yaml yaml = this.yamlHolder.get();
        Node node;

        try {
            node = yaml.compose(reader);
        } catch (YAMLException e) {
            return DecodeError.fatalError(e).asFailure();
        }

        return (Result<T, DecodeError>) decoder.decode(this.nodeIn(node));
    }

    @Override
    public <T> Result<Void, EncodeError> encodeTo(Writer writer, Encoder<? super T> encoder, @UnknownNullability T input) {
        Result<Node, EncodeError> encodeResult = encoder.encode(this.nodeOut(), input);
        if (encodeResult.isFailure()) {
            return encodeResult.asFailure();
        }

        Node node = encodeResult.unwrap();
        Yaml yaml = this.yamlHolder.get();

        try {
            yaml.serialize(node, writer);
        } catch (YAMLException e) {
            return EncodeError.fatalError(e).asFailure();
        }

        return Result.success();
    }
}
