package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.Iterator;
import java.util.Optional;

/**
 * Retrieve the head element of an <code>Iterable</code>, wrapped in an <code>Optional</code>. If the
 * <code>Iterable</code> is empty, the result is <code>Optional.empty()</code>.
 *
 * @param <A> the Iterable element type
 */
public final class Head<A> implements Fn1<Iterable<A>, Optional<A>> {

    private Head() {
    }

    @Override
    public Optional<A> apply(Iterable<A> as) {
        Iterator<A> iterator = as.iterator();
        return iterator.hasNext()
                ? Optional.of(iterator.next())
                : Optional.empty();
    }

    public static <A> Head<A> head() {
        return new Head<>();
    }

    public static <A> Optional<A> head(Iterable<A> as) {
        return Head.<A>head().apply(as);
    }
}
