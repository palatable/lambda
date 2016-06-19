package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Identity.id;

/**
 * A dually-parametric <code>Functor</code> that maps contravariantly over the first parameter and covariantly over the
 * second.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Profunctor" target="_top">Profunctors</a>.
 *
 * @param <A> The type of the first parameter
 * @param <B> The type of the second parameter
 * @see Functor
 * @see Bifunctor
 * @see com.jnape.palatable.lambda.functions.DyadicFunction
 */
@FunctionalInterface
public interface Profunctor<A, B> {

    default <C> Profunctor<C, B> diMapL(MonadicFunction<? super C, ? extends A> fn) {
        return diMap(fn, id());
    }

    default <C> Profunctor<A, C> diMapR(MonadicFunction<? super B, ? extends C> fn) {
        return diMap(id(), fn);
    }

    <C, D> Profunctor<C, D> diMap(MonadicFunction<? super C, ? extends A> f1,
                                  MonadicFunction<? super B, ? extends D> f2);
}
