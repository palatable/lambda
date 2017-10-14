package com.jnape.palatable.lambda.traversable;

import java.util.Optional;

/**
 * Static factory methods for adapting core JDK types to Lambda interfaces.
 */
public final class Lambda {

    private Lambda() {
    }

    /**
     * Wrap an {@link Iterable} in a {@link LambdaIterable}.
     *
     * @param as  the Iterable
     * @param <A> the Iterable element type
     * @return a LambdaIterable
     */
    public static <A> LambdaIterable<A> lambda(Iterable<? extends A> as) {
        return LambdaIterable.wrap(as);
    }

    /**
     * Wrap an {@link Optional} in a {@link LambdaOptional}.
     *
     * @param opt the Optional
     * @param <A> the Optional type
     * @return a LambdaOptional
     */
    public static <A> LambdaOptional<A> lambda(Optional<A> opt) {
        return LambdaOptional.wrap(opt);
    }
}
