package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.iterators.PredicatedDroppingIterator;

/**
 * Lazily limit the <code>Iterable</code> by skipping the first contiguous group of elements that satisfy the predicate,
 * beginning iteration at the first element for which the predicate evaluates to <code>false</code>.
 *
 * @param <A> The Iterable element type
 * @see Drop
 * @see Filter
 * @see TakeWhile
 */

public final class DropWhile<A> implements DyadicFunction<MonadicFunction<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    private DropWhile() {
    }

    @Override
    public Iterable<A> apply(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return () -> new PredicatedDroppingIterator<>(predicate, as.iterator());
    }

    public static <A> DropWhile<A> dropWhile() {
        return new DropWhile<>();
    }

    public static <A> MonadicFunction<Iterable<A>, Iterable<A>> dropWhile(
            MonadicFunction<? super A, Boolean> predicate) {
        return DropWhile.<A>dropWhile().apply(predicate);
    }

    public static <A> Iterable<A> dropWhile(MonadicFunction<? super A, Boolean> predicate, Iterable<A> as) {
        return DropWhile.<A>dropWhile(predicate).apply(as);
    }
}
