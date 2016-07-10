package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A dually-parametric <code>Functor</code> that maps covariantly over both parameters.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Bifunctor" target="_top">Bifunctors</a>.
 *
 * @param <A> The type of the first parameter
 * @param <B> The type of the second parameter
 * @see Functor
 * @see Profunctor
 * @see com.jnape.palatable.lambda.adt.hlist.Tuple2
 */
@FunctionalInterface
public interface Bifunctor<A, B> {

    default <C> Bifunctor<C, B> biMapL(Fn1<? super A, ? extends C> fn) {
        return biMap(fn, id());
    }

    default <C> Bifunctor<A, C> biMapR(Fn1<? super B, ? extends C> fn) {
        return biMap(id(), fn);
    }

    <C, D> Bifunctor<C, D> biMap(Fn1<? super A, ? extends C> lFn,
                                 Fn1<? super B, ? extends D> rFn);
}
