package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * A specialized {@link Fn2} that returns a Boolean when fully applied, or a {@link Predicate} when partially applied.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 */
@FunctionalInterface
public interface BiPredicate<A, B> extends Fn2<A, B, Boolean> {

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
    default <D> BiPredicate<A, B> discardR(Applicative<D, Fn1<A, ?>> appB) {
        return Fn2.super.discardR(appB)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Predicate<? super Product2<? extends A, ? extends B>> uncurry() {
        return Fn2.super.uncurry()::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> BiPredicate<Z, B> diMapL(Fn1<? super Z, ? extends A> fn) {
        return Fn2.super.diMapL(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Fn2<Z, B, Boolean> contraMap(Fn1<? super Z, ? extends A> fn) {
        return Fn2.super.contraMap(fn)::apply;
    }

    /**
     * Left-to-right short-circuiting logical conjunction.
     *
     * @param other the biPredicate to test if this one succeeds
     * @return a biPredicate representing the conjunction of this biPredicate and other
     */
    default BiPredicate<A, B> and(BiPredicate<? super A, ? super B> other) {
        return (a, b) -> apply(a, b) && other.apply(a, b);
    }

    /**
     * Left-to-right short-circuiting logical disjunction.
     *
     * @param other the biPredicate to test if this one fails
     * @return a biPredicate representing the disjunction of this biPredicate and other
     */
    default BiPredicate<A, B> or(BiPredicate<? super A, ? super B> other) {
        return (a, b) -> apply(a, b) || other.apply(a, b);
    }

    /**
     * Logical negation.
     *
     * @return the negation of this biPredicate
     */
    default BiPredicate<A, B> negate() {
        return (a, b) -> !apply(a, b);
    }

    /**
     * Convert this {@link BiPredicate} to a java {@link java.util.function.BiPredicate}.
     *
     * @return {@link java.util.function.BiPredicate}
     */
    default java.util.function.BiPredicate<A, B> toBiPredicate() {
        return this::apply;
    }

    /**
     * Create a {@link BiPredicate} from a java {@link java.util.function.BiPredicate}.
     *
     * @param biPredicate the {@link java.util.function.BiPredicate}
     * @param <A>         the first input type
     * @param <B>         the second input type
     * @return the {@link BiPredicate}
     */
    static <A, B> BiPredicate<A, B> fromBiPredicate(java.util.function.BiPredicate<A, B> biPredicate) {
        return biPredicate::test;
    }
}
