package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.adt.Maybe;

import java.util.Optional;

/**
 * Static factory methods for adapting core JDK types to {@link Traversable}.
 *
 * @deprecated in favor of {@link LambdaIterable} and {@link Maybe}.
 */
@Deprecated
public final class Traversables {

    private Traversables() {
    }

    /**
     * Wrap an {@link Iterable} in a {@link Traversable}.
     *
     * @param as  the Iterable
     * @param <A> the Iterable element type
     * @return a Traversable wrapper around as
     * @deprecated in favor of {@link LambdaIterable#wrap(Iterable)}
     */
    @Deprecated
    public static <A> TraversableIterable<A> traversable(Iterable<? extends A> as) {
        return TraversableIterable.wrap(as);
    }

    /**
     * Wrap an {@link Optional} in a {@link Traversable}.
     *
     * @param opt the Optional
     * @param <A> the Optional type
     * @return a Traversable wrapper around opt
     * @deprecated in favor of {@link Maybe#fromOptional(Optional)}
     */
    @Deprecated
    public static <A> TraversableOptional<A> traversable(Optional<A> opt) {
        return TraversableOptional.wrap(opt);
    }
}
