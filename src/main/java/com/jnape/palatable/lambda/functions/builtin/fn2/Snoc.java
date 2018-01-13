package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.SnocIterator;

/**
 * Opposite of {@link Cons}: lazily append an element to the end of the given {@link Iterable}.
 * <p>
 * Note that obtaining both laziness and stack-safety is particularly tricky here, and requires an initial eager
 * deforestation of <code>O(k)</code> traversals where <code>k</code> is the number of contiguously nested
 * {@link Snoc}s.
 *
 * @param <A> the Iterable element type
 */
public final class Snoc<A> implements Fn2<A, Iterable<A>, Iterable<A>> {

    private static final Snoc INSTANCE = new Snoc();

    private Snoc() {
    }

    @Override
    public Iterable<A> apply(A a, Iterable<A> as) {
        return () -> new SnocIterator<>(a, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Snoc<A> snoc() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> snoc(A a) {
        return Snoc.<A>snoc().apply(a);
    }

    public static <A> Iterable<A> snoc(A a, Iterable<A> as) {
        return snoc(a).apply(as);
    }
}
