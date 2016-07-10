package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

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
 * @see Fn1
 */
@FunctionalInterface
public interface Profunctor<A, B> {

    default <C> Profunctor<C, B> diMapL(Fn1<C, A> fn) {
        return diMap(fn, id());
    }

    default <C> Profunctor<A, C> diMapR(Fn1<B, C> fn) {
        return diMap(id(), fn);
    }

    <C, D> Profunctor<C, D> diMap(Fn1<C, A> lFn, Fn1<B, D> rFn);
}
