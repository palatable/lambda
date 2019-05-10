package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.internal.iteration.PredicatedDroppingIterable;

/**
 * Lazily limit the <code>Iterable</code> by skipping the first contiguous group of elements that satisfy the predicate,
 * beginning iteration at the first element for which the predicate evaluates to <code>false</code>.
 *
 * @param <A> The Iterable element type
 * @see Drop
 * @see Filter
 * @see TakeWhile
 */

public final class DropWhile<A> implements Fn2<Fn1<? super A, ? extends Boolean>, Iterable<A>, Iterable<A>> {

    private static final DropWhile<?> INSTANCE = new DropWhile<>();

    private DropWhile() {
    }

    @Override
    public Iterable<A> checkedApply(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return new PredicatedDroppingIterable<>(predicate, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> DropWhile<A> dropWhile() {
        return (DropWhile<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> dropWhile(Fn1<? super A, ? extends Boolean> predicate) {
        return DropWhile.<A>dropWhile().apply(predicate);
    }

    public static <A> Iterable<A> dropWhile(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return DropWhile.<A>dropWhile(predicate).apply(as);
    }
}
