package com.jnape.palatable.lambda.functor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A dually-parametric functor that maps covariantly over both parameters.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Bifunctor" target="_top">Bifunctors</a>.
 *
 * @param <A>  The type of the left parameter
 * @param <B>  The type of the right parameter
 * @param <BF> The unification parameter
 * @see Functor
 * @see Profunctor
 * @see com.jnape.palatable.lambda.adt.hlist.Tuple2
 */
@FunctionalInterface
public interface Bifunctor<A, B, BF extends Bifunctor> extends BoundedBifunctor<A, B, Object, Object, BF> {

    /**
     * Covariantly map over the left parameter.
     *
     * @param <C> the new left parameter type
     * @param fn  the mapping function
     * @return a bifunctor over C (the new left parameter) and B (the same right parameter)
     */
    default <C> Bifunctor<C, B, BF> biMapL(Function<? super A, ? extends C> fn) {
        return biMap(fn, id());
    }

    /**
     * Covariantly map over the right parameter. For all bifunctors that are also functors, it should hold that
     * <code>biMapR(f) == fmap(f)</code>.
     *
     * @param <C> the new right parameter type
     * @param fn  the mapping function
     * @return a bifunctor over A (the same left parameter) and C (the new right parameter)
     */
    default <C> Bifunctor<A, C, BF> biMapR(Function<? super B, ? extends C> fn) {
        return biMap(id(), fn);
    }

    /**
     * Dually map covariantly over both the left and right parameters. This is isomorphic to
     * <code>biMapL(lFn).biMapR(rFn)</code>.
     *
     * @param <C> the new left parameter type
     * @param <D> the new right parameter type
     * @param lFn the left parameter mapping function
     * @param rFn the right parameter mapping function
     * @return a bifunctor over C (the new left parameter type) and D (the new right parameter type)
     */
    <C, D> Bifunctor<C, D, BF> biMap(Function<? super A, ? extends C> lFn, Function<? super B, ? extends D> rFn);
}
