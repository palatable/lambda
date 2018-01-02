package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.Function;

/**
 * A specialized {@link Fn2} that returns a Boolean when fully applied,
 * or a {@link Predicate} when partially applied.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 */
@FunctionalInterface
public interface BiPredicate<A, B> extends Fn2<A, B, Boolean>, java.util.function.BiPredicate<A, B> {

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(A a, B b) {
        return apply(a, b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Predicate<B> apply(A a) {
        return Fn2.super.apply(a)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default BiPredicate<B, A> flip() {
        return Fn2.super.flip()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Predicate<Tuple2<A, B>> uncurry() {
        return Fn2.super.uncurry()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> BiPredicate<Z, B> diMapL(Function<? super Z, ? extends A> fn) {
        return Fn2.super.diMapL(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Fn2<Z, B, Boolean> contraMap(Function<? super Z, ? extends A> fn) {
        return Fn2.super.contraMap(fn)::apply;
    }

    /**
     * Override of {@link java.util.function.BiPredicate#and(java.util.function.BiPredicate)}, returning an instance of
     * <code>BiPredicate</code> for compatibility. Left-to-right composition.
     *
     * @param other the biPredicate to test if this one succeeds
     * @return a biPredicate representing the conjunction of this biPredicate and other
     */
    @Override
    default BiPredicate<A, B> and(java.util.function.BiPredicate<? super A, ? super B> other) {
        return (a, b) -> apply(a, b) && other.test(a, b);
    }

    /**
     * Override of {@link java.util.function.BiPredicate#or(java.util.function.BiPredicate)}, returning an instance of
     * <code>BiPredicate</code> for compatibility. Left-to-right composition.
     *
     * @param other the biPredicate to test if this one fails
     * @return a biPredicate representing the disjunction of this biPredicate and other
     */
    @Override
    default BiPredicate<A, B> or(java.util.function.BiPredicate<? super A, ? super B> other) {
        return (a, b) -> apply(a, b) || other.test(a, b);
    }

    /**
     * Override of {@link java.util.function.BiPredicate#negate()}, returning an instance of <code>BiPredicate</code>
     * for compatibility.
     *
     * @return the negation of this biPredicate
     */
    @Override
    default BiPredicate<A, B> negate() {
        return (a, b) -> !apply(a, b);
    }
}
