package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn1;

/**
 * A specialized {@link Fn1} that returns a <code>Boolean</code>.
 *
 * @param <A> The argument type
 */
public interface Predicate<A> extends Fn1<A, Boolean>, java.util.function.Predicate<A> {

    @Override
    default boolean test(A a) {
        return apply(a);
    }

    @Override
    default Predicate<A> and(java.util.function.Predicate<? super A> other) {
        return a -> apply(a) && other.test(a);
    }

    @Override
    default Predicate<A> or(java.util.function.Predicate<? super A> other) {
        return a -> apply(a) || other.test(a);
    }

    @Override
    default Predicate<A> negate() {
        return a -> !apply(a);
    }
}
