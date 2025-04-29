package dev.siroshun.codec4j.api.codec.collection;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.api.error.EncodeError;
import dev.siroshun.codec4j.api.io.In;
import dev.siroshun.codec4j.api.io.Out;
import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Iterator;

/**
 * An interface to processing elements of the collection.
 *
 * @param <E> the type of element
 * @param <C> the type of collection
 */
public interface ElementProcessor<E, C> {

    /**
     * Encodes an element to the provided {@link Out}.
     *
     * @param out     the {@link Out} for writing the encoded element
     * @param element the element to encode to the {@link Out}
     * @param <O>     the type of the output destination
     * @return a result containing {@code null} if the operation succeeded, or a {@link EncodeError} if the operation failed
     */
    <O> @NotNull Result<O, EncodeError> encodeElement(@NotNull Out<O> out, @UnknownNullability E element);

    /**
     * Creates an {@link Iterator} from {@link C} for encoding elements.
     *
     * @param input the object to encode
     * @return the {@link Iterator} of elements
     */
    @NotNull Iterator<E> toIterator(@UnknownNullability C input);

    /**
     * Decodes an element from the provided {@link In}.
     *
     * @param in the {@link In} for reading the element to decode
     * @return a result containing the decoded element, or a {@link DecodeError} if the operation failed
     */
    @NotNull Result<E, DecodeError> decodeElement(@NotNull In in);

    /**
     * Creates a {@link C} for an identity of {@link In#readList(Object, java.util.function.BiFunction)}.
     *
     * @return a {@link C} for an identity of {@link In#readList(Object, java.util.function.BiFunction)}
     */
    @NotNull C createIdentity();

    /**
     * Accepts the element to the identity object of {@link C}.
     *
     * @param identity the identity object
     * @param element  the element
     * @return the {@link Result} of accepting the element
     */
    @NotNull Result<Void, DecodeError> acceptElement(@NotNull C identity, @UnknownNullability E element);

    /**
     * Finalizes the identity object of {@link C}.
     *
     * @param identity the identity object
     * @return the final {@link C} object
     */
    @NotNull C finalizeIdentity(@NotNull C identity);

}
