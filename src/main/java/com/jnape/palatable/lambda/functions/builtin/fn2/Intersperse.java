package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Tail.tail;
import static com.jnape.palatable.lambda.functions.builtin.fn2.PrependAll.prependAll;

/**
 * Lazily inject the provided separator value between each value in the supplied <code>Iterable</code>. An empty
 * <code>Iterable</code> is left untouched.
 *
 * @param <A> the Iterable parameter type
 * @see PrependAll
 */
public final class Intersperse<A> implements Fn2<A, Iterable<A>, Iterable<A>> {

    private static final Intersperse<?> INSTANCE = new Intersperse<>();

    private Intersperse() {
    }

    @Override
    public Iterable<A> checkedApply(A a, Iterable<A> as) {
        return tail(prependAll(a, as));
    }

    @SuppressWarnings("unchecked")
    public static <A> Intersperse<A> intersperse() {
        return (Intersperse<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> intersperse(A a) {
        return Intersperse.<A>intersperse().apply(a);
    }

    public static <A> Iterable<A> intersperse(A a, Iterable<A> as) {
        return intersperse(a).apply(as);
    }
}
