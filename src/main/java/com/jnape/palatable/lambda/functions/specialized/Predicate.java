package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

/**
 * A specialized {@link Fn1} that returns a <code>Boolean</code>.
 *
 * @param <A> The argument type
 */
public interface Predicate<A> extends Fn1<A, Boolean>, java.util.function.Predicate<A> {

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean test(A a) {
        return apply(a);
    }

    /**
     * Override of {@link Function#compose(Function)}, returning an instance of <code>Predicate</code> for
     * compatibility. Right-to-left composition.
     *
     * @param before the function who's return value is this predicate's argument
     * @param <Z>    the new argument type
     * @return a new predicate of Z (the new argument type)
     */
    @Override
    default <Z> Predicate<Z> compose(Function<? super Z, ? extends A> before) {
        return Fn1.super.compose(before)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Predicate<Z> diMapL(Function<? super Z, ? extends A> fn) {
        return Fn1.super.diMapL(fn)::apply;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> Predicate<Z> contraMap(Function<? super Z, ? extends A> fn) {
        return Fn1.super.contraMap(fn)::apply;
    }

    /**
     * Override of {@link java.util.function.Predicate#and(java.util.function.Predicate)}, returning an instance of
     * <code>Predicate</code> for compatibility. Left-to-right composition.
     *
     * @param other the predicate to test if this one succeeds
     * @return a predicate representing the conjunction of this predicate and other
     */
    @Override
    default Predicate<A> and(java.util.function.Predicate<? super A> other) {
        return a -> apply(a) && other.test(a);
    }

    /**
     * Override of {@link java.util.function.Predicate#or(java.util.function.Predicate)}, returning an instance of
     * <code>Predicate</code> for compatibility. Left-to-right composition.
     *
     * @param other the predicate to test if this one fails
     * @return a predicate representing the disjunction of this predicate and other
     */
    @Override
    default Predicate<A> or(java.util.function.Predicate<? super A> other) {
        return a -> apply(a) || other.test(a);
    }

    /**
     * Override of {@link java.util.function.Predicate#negate()}, returning an instance of <code>Predicate</code> for
     * compatibility.
     *
     * @return the negation of this predicate
     */
    @Override
    default Predicate<A> negate() {
        return a -> !apply(a);
    }

    /**
     * Static factory method to create a predicate from a function.
     *
     * @param predicate the function
     * @param <A>       the input type
     * @return the predicate
     */
    static <A> Predicate<A> predicate(Function<? super A, ? extends Boolean> predicate) {
        return predicate::apply;
    }
}
