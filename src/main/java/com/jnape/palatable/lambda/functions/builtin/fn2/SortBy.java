package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Sort;

import java.util.List;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.SortWith.sortWith;
import static java.util.Comparator.comparing;

/**
 * Given an {@link Iterable} and some mapping function from the {@link Iterable} element type to some
 * {@link Comparable} type, produce a sorted {@link List} of the original elements based on sorting applied to the
 * result of the mapping function. Note that this is both eager and monolithic.
 *
 * @param <A> the input Iterable and output List element type
 * @param <B> the mapped Comparable type
 * @see Sort
 * @see SortWith
 */
public final class SortBy<A, B extends Comparable<B>> implements Fn2<Function<? super A, ? extends B>, Iterable<A>, List<A>> {

    private static final SortBy INSTANCE = new SortBy();

    private SortBy() {
    }

    @Override
    public List<A> apply(Function<? super A, ? extends B> fn, Iterable<A> as) {
        return sortWith(comparing(fn), as);
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> SortBy<A, B> sortBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> Fn1<Iterable<A>, List<A>> sortBy(
            Function<? super A, ? extends B> fn) {
        return SortBy.<A, B>sortBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> List<A> sortBy(Function<? super A, ? extends B> fn, Iterable<A> as) {
        return SortBy.<A, B>sortBy(fn).apply(as);
    }
}
