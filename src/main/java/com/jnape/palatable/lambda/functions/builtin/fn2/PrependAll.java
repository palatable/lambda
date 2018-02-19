package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iteration.PrependingIterator;

/**
 * Lazily prepend each value with of the <code>Iterable</code> with the supplied separator value. An empty
 * <code>Iterable</code> is left untouched.
 *
 * @param <A> the Iterable parameter type
 * @see Intersperse
 */
public final class PrependAll<A> implements Fn2<A, Iterable<A>, Iterable<A>> {

    private static final PrependAll INSTANCE = new PrependAll();

    private PrependAll() {
    }

    @Override
    public Iterable<A> apply(A a, Iterable<A> as) {
        return () -> new PrependingIterator<>(a, as.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <A> PrependAll<A> prependAll() {
        return INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> prependAll(A a) {
        return PrependAll.<A>prependAll().apply(a);
    }

    public static <A> Iterable<A> prependAll(A a, Iterable<A> as) {
        return prependAll(a).apply(as);
    }
}
