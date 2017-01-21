package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A dually-parametric functor that maps contravariantly over the left parameter and covariantly over the
 * right.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Profunctor" target="_top">Profunctors</a>.
 *
 * @param <A>  The type of the left parameter
 * @param <B>  The type of the right parameter
 * @param <PF> The unification parameter
 * @see Functor
 * @see Bifunctor
 * @see Fn1
 */
@FunctionalInterface
public interface Profunctor<A, B, PF extends Profunctor> {

    /**
     * Contravariantly map over the left parameter.
     *
     * @param <Z> the new left parameter type
     * @param fn  the mapping function
     * @return a profunctor over Z (the new left parameter type) and C (the same right parameter type)
     */
    default <Z> Profunctor<Z, B, PF> diMapL(Function<Z, A> fn) {
        return diMap(fn, id());
    }

    /**
     * Covariantly map over the right parameter. For all profunctors that are also functors, it should hold that
     * <code>diMapR(f) == fmap(f)</code>.
     *
     * @param <C> the new right parameter type
     * @param fn  the mapping function
     * @return a profunctor over A (the same left parameter type) and C (the new right parameter type)
     */
    default <C> Profunctor<A, C, PF> diMapR(Function<B, C> fn) {
        return diMap(id(), fn);
    }

    /**
     * Dually map contravariantly over the left parameter and covariantly over the right parameter. This is isomorphic
     * to <code>diMapL(lFn).diMapR(rFn)</code>.
     *
     * @param <Z> the new left parameter type
     * @param <C> the new right parameter type
     * @param lFn the left parameter mapping function
     * @param rFn the right parameter mapping function
     * @return a profunctor over Z (the new left parameter type) and C (the new right parameter type)
     */
    <Z, C> Profunctor<Z, C, PF> diMap(Function<Z, A> lFn, Function<B, C> rFn);
}
