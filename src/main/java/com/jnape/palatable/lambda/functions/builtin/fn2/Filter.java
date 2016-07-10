package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iterators.FilteringIterator;

/**
 * Lazily apply a predicate to each element in an <code>Iterable</code>, returning an <code>Iterable</code> of just the
 * elements for which the predicate evaluated to <code>true</code>.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @see TakeWhile
 * @see DropWhile
 */
public final class Filter<A> implements Fn2<Fn1<? super A, Boolean>, Iterable<A>, Iterable<A>> {

    private Filter() {
    }

    @Override
    public Iterable<A> apply(Fn1<? super A, Boolean> predicate, Iterable<A> as) {
        return () -> new FilteringIterator<>(predicate, as.iterator());
    }

    public static <A> Filter<A> filter() {
        return new Filter<>();
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> filter(Fn1<? super A, Boolean> predicate) {
        return Filter.<A>filter().apply(predicate);
    }

    public static <A> Iterable<A> filter(Fn1<? super A, Boolean> predicate, Iterable<A> as) {
        return Filter.<A>filter(predicate).apply(as);
    }
}
