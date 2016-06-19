package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.MonadicFunction;

/**
 * An interface for the generic covariant functorial operation <code>map</code> over some parameter <code>A</code>.
 * Functors are foundational to many of the classes provided by this library; generally, anything that can be thought of
 * as "mappable" is an instance of at least this interface.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Functor" target="_top">Functors</a>.
 *
 * @param <A> The type of the parameter
 * @see Bifunctor
 * @see Profunctor
 * @see MonadicFunction
 * @see com.jnape.palatable.lambda.adt.tuples.Tuple2
 * @see com.jnape.palatable.lambda.adt.Either
 */
@FunctionalInterface
public interface Functor<A> {

    <B> Functor<B> fmap(MonadicFunction<? super A, ? extends B> fn);
}
