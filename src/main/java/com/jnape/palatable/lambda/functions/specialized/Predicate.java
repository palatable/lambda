package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;

/**
 * A specialized {@link Fn1} that returns a <code>Boolean</code>.
 *
 * @param <A> The argument type
 */
@FunctionalInterface
public interface Predicate<A> extends Fn1<A, Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Predicate<Z> diMapL(Fn1<? super Z, ? extends A> fn) {
        return Fn1.super.diMapL(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Predicate<Z> contraMap(Fn1<? super Z, ? extends A> fn) {
        return Fn1.super.contraMap(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> BiPredicate<Z, A> widen() {
        return Fn1.super.widen()::checkedApply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> Predicate<A> discardR(Applicative<C, Fn1<A, ?>> appB) {
        return Fn1.super.discardR(appB)::checkedApply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Y, Z> BiPredicate<Y, Z> compose(Fn2<? super Y, ? super Z, ? extends A> before) {
        return Fn1.super.compose(before)::apply;
    }

    /**
     * Left-to-right short-circuiting logical conjunction.
     *
     * @param other the predicate to test if this one succeeds
     * @return a predicate representing the conjunction of this predicate and other
     */
    default Predicate<A> and(Predicate<? super A> other) {
        return a -> apply(a) && other.apply(a);
    }

    /**
     * Left-to-right short-circuiting logical disjunction.
     *
     * @param other the predicate to test if this one fails
     * @return a predicate representing the disjunction of this predicate and other
     */
    default Predicate<A> or(Predicate<? super A> other) {
        return a -> apply(a) || other.apply(a);
    }

    /**
     * Logical negation.
     *
     * @return the negation of this predicate
     */
    default Predicate<A> negate() {
        return a -> !apply(a);
    }

    /**
     * Convert this {@link Predicate} to a java {@link java.util.function.Predicate}.
     *
     * @return the {@link java.util.function.Predicate}
     */
    default java.util.function.Predicate<A> toPredicate() {
        return this::apply;
    }

    /**
     * Static factory method to create a predicate from an {@link Fn1}.
     *
     * @param predicate the {@link Fn1}
     * @param <A>       the input type
     * @return the predicate
     */
    static <A> Predicate<A> predicate(Fn1<? super A, ? extends Boolean> predicate) {
        return predicate::apply;
    }

    /**
     * Create a {@link Predicate} from a java {@link java.util.function.Predicate}.
     *
     * @param predicate the java {@link java.util.function.Predicate}
     * @param <A>       the input type
     * @return the {@link Predicate}
     */
    static <A> Predicate<A> fromPredicate(java.util.function.Predicate<A> predicate) {
        return predicate::test;
    }
}
