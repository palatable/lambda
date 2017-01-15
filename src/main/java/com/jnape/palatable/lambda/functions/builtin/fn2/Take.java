package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.TakingIterator;

/**
 * Lazily limit the <code>Iterable</code> to <code>n</code> elements by returning an <code>Iterable</code> that stops
 * iteration after the <code>nth</code> element, or the last element of the <code>Iterable</code>, whichever comes
 * first.
 *
 * @param <A> The Iterable element type
 * @see TakeWhile
 * @see Drop
 */
public final class Take<A> implements Fn2<Integer, Iterable<A>, Iterable<A>> {

    private static final Take INSTANCE = new Take();

    private Take() {
    }

    @Override
    public Iterable<A> apply(Integer n, Iterable<A> as) {
        return () -> new TakingIterator<>(n, as.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A> Take<A> take() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> take(int n) {
        return Take.<A>take().apply(n);
    }

    public static <A> Iterable<A> take(int n, Iterable<A> as) {
        return Take.<A>take(n).apply(as);
    }
}
