package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.iteration.FilteringIterable;

import java.util.function.Function;

/**
 * Lazily apply a predicate to each element in an <code>Iterable</code>, returning an <code>Iterable</code> of just the
 * elements for which the predicate evaluated to <code>true</code>.
 *
 * @param <A> A type contravariant to the input Iterable element type
 * @see TakeWhile
 * @see DropWhile
 */
public final class Filter<A> implements Fn2<Function<? super A, ? extends Boolean>, Iterable<A>, Iterable<A>> {

    private static final Filter<?> INSTANCE = new Filter<>();

    private Filter() {
    }

    @Override
    public Iterable<A> apply(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return new FilteringIterable<>(predicate, as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Filter<A> filter() {
        return (Filter<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, Iterable<A>> filter(Function<? super A, ? extends Boolean> predicate) {
        return Filter.<A>filter().apply(predicate);
    }

    public static <A> Iterable<A> filter(Function<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return Filter.<A>filter(predicate).apply(as);
    }
}
