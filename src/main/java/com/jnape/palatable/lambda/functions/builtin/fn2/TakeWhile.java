package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.PredicatedTakingIterator;

/**
 * Lazily limit the <code>Iterable</code> to the first group of contiguous elements that satisfy the predicate by
 * iterating up to, but not including, the first element for which the predicate evaluates to <code>false</code>.
 *
 * @param <A> The Iterable element type
 * @see Take
 * @see Filter
 * @see DropWhile
 */
public final class TakeWhile<A> implements Fn2<Fn1<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    private TakeWhile() {
    }

    @Override
    public Iterable<A> apply(Fn1<? super A, Boolean> predicate, Iterable<A> as) {
        return () -> new PredicatedTakingIterator<>(predicate, as.iterator());
    }

    public static <A> TakeWhile<A> takeWhile() {
        return new TakeWhile<>();
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> takeWhile(
            Fn1<? super A, Boolean> predicate) {
        return TakeWhile.<A>takeWhile().apply(predicate);
    }

    public static <A> Iterable<A> takeWhile(Fn1<? super A, Boolean> predicate, Iterable<A> as) {
        return TakeWhile.<A>takeWhile(predicate).apply(as);
    }
}
