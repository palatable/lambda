package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iteration.ConsingIterator;

/**
 * Prepend an element to an <code>Iterable</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Cons<A> implements Fn2<A, Iterable<A>, Iterable<A>> {

    private static final Cons<?> INSTANCE = new Cons<>();

    private Cons() {
    }

    @Override
    public Iterable<A> apply(A a, Iterable<A> as) {
        return () -> new ConsingIterator<>(a, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Cons<A> cons() {
        return (Cons<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> cons(A a) {
        return Cons.<A>cons().apply(a);
    }

    public static <A> Iterable<A> cons(A a, Iterable<A> as) {
        return cons(a).apply(as);
    }
}
