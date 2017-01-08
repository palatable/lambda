package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Optional;

/**
 * Retrieve the last element of an <code>Iterable</code>, wrapped in an <code>Optional</code>. If the
 * <code>Iterable</code> is empty, the result is <code>Optional.empty()</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Last<A> implements Fn1<Iterable<A>, Optional<A>> {

    private static final Last INSTANCE = new Last();

    private Last() {
    }

    @Override
    public Optional<A> apply(Iterable<A> as) {
        A last = null;
        for (A a : as) {
            last = a;
        }
        return Optional.ofNullable(last);
    }

    @SuppressWarnings("unchecked")
    public static <A> Last<A> last() {
        return (Last<A>) INSTANCE;
    }

    public static <A> Optional<A> last(Iterable<A> as) {
        return Last.<A>last().apply(as);
    }
}
