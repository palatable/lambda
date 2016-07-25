package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A dually-parametric functor that maps contravariantly over the left parameter and covariantly over the
 * right.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Profunctor" target="_top">Profunctors</a>.
 *
 * @param <A> The type of the left parameter
 * @param <B> The type of the right parameter
 * @see Functor
 * @see Bifunctor
 * @see Fn1
 */
@FunctionalInterface
public interface Profunctor<A, B> {

    /**
     * Contravariantly map over the left parameter.
     *
     * @param fn  the mapping function
     * @param <Z> the new left parameter type
     * @return a profunctor over Z (the new left parameter type) and C (the same right parameter type)
     */
    default <Z> Profunctor<Z, B> diMapL(Fn1<Z, A> fn) {
        return diMap(fn, id());
    }

    /**
     * Covariantly map over the right parameter. For all profunctors that are also functors, it should hold that
     * <code>diMapR(f) == fmap(f)</code>.
     *
     * @param fn  the mapping function
     * @param <C> the new right parameter type
     * @return a profunctor over A (the same left parameter type) and C (the new right parameter type)
     */
    default <C> Profunctor<A, C> diMapR(Fn1<B, C> fn) {
        return diMap(id(), fn);
    }

    /**
     * Dually map contravariantly over the left parameter and covariantly over the right parameter. This is isomorphic
     * to <code>diMapL(lFn).diMapR(rFn)</code>.
     *
     * @param lFn the left parameter mapping function
     * @param rFn the right parameter mapping function
     * @param <Z> the new left parameter type
     * @param <C> the new right parameter type
     * @return a profunctor over Z (the new left parameter type) and C (the new right parameter tyep)
     */
    <Z, C> Profunctor<Z, C> diMap(Fn1<Z, A> lFn, Fn1<B, C> rFn);
}
