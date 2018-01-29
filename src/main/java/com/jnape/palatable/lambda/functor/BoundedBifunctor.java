package com.jnape.palatable.lambda.functor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A {@link Bifunctor} that has both parameter types upper bounded; that is, neither parameters can be mapped to a value
 * that is not covariant to their respective upper bounds
 *
 * @param <A>       The type of the left parameter
 * @param <B>       The type of the right parameter
 * @param <ContraA> The type of the left parameter upper type bound
 * @param <ContraB> The type of the right parameter upper type bound
 * @param <BF>      The unification parameter
 * @see Bifunctor
 */
@FunctionalInterface
public interface BoundedBifunctor<A extends ContraA, B extends ContraB, ContraA, ContraB, BF extends BoundedBifunctor> {

    /**
     * Covariantly map the left parameter into a value that is covariant to <code>ContraA</code>.
     *
     * @param fn  the mapping function
     * @param <C> the new left parameter type
     * @return a bifunctor of C (the new parameter type) and B (the same right parameter)
     */
    default <C extends ContraA> BoundedBifunctor<C, B, ContraA, ContraB, BF> biMapL(
            Function<? super A, ? extends C> fn) {
        return biMap(fn, id());
    }

    /**
     * Covariantly map the right parameter into a value that is covariant to <code>ContraB</code>.
     *
     * @param fn  the mapping function
     * @param <C> the new right parameter type
     * @return a bifunctor of A (the same left parameter) and C (the new parameter type)
     */
    default <C extends ContraB> BoundedBifunctor<A, C, ContraA, ContraB, BF> biMapR(
            Function<? super B, ? extends C> fn) {
        return biMap(id(), fn);
    }

    /**
     * Dually covariantly map both the left and right parameters into values that are covariant to <code>ContraA</code>
     * and <code>ContraB</code>, respectively. This is isomorphic to <code>biMapL(lFn).biMapR(rFn)</code>.
     *
     * @param <C> the new left parameter type
     * @param <D> the new right parameter type
     * @param lFn the left parameter mapping function
     * @param rFn the right parameter mapping function
     * @return a bifunctor over C (the new left parameter type) and D (the new right parameter type)
     */
    <C extends ContraA, D extends ContraB> BoundedBifunctor<C, D, ContraA, ContraB, BF> biMap(
            Function<? super A, ? extends C> lFn,
            Function<? super B, ? extends D> rFn);
}
