package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.internal.iteration.FlatteningIterator;

/**
 * Given a nested {@link Iterable} of {@link Iterable}s, return a lazily flattening {@link Iterable}
 * of the nested elements.
 *
 * @param <A> the nested Iterable element type
 */
public final class Flatten<A> implements Fn1<Iterable<? extends Iterable<? extends A>>, Iterable<A>> {
    private static final Flatten<?> INSTANCE = new Flatten<>();

    private Flatten() {
    }

    @Override
    public Iterable<A> checkedApply(Iterable<? extends Iterable<? extends A>> iterables) {
        return () -> new FlatteningIterator<>(iterables.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A> Flatten<A> flatten() {
        return (Flatten<A>) INSTANCE;
    }

    public static <A> Iterable<A> flatten(Iterable<? extends Iterable<? extends A>> as) {
        return Flatten.<A>flatten().apply(as);
    }
}
