package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.SortBy;
import com.jnape.palatable.lambda.functions.builtin.fn2.SortWith;

import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.SortBy.sortBy;

/**
 * Given an {@link Iterable} of {@link Comparable} elements, return a {@link List} of the sorted elements. Note that
 * this is both eager and monolithic.
 *
 * @param <A> the input Iterable and output List element type
 * @see SortBy
 * @see SortWith
 */
public final class Sort<A extends Comparable<A>> implements Fn1<Iterable<A>, List<A>> {

    private static final Sort<?> INSTANCE = new Sort<>();

    private Sort() {
    }

    @Override
    public List<A> checkedApply(Iterable<A> as) {
        return sortBy(id(), as);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Sort<A> sort() {
        return (Sort<A>) INSTANCE;
    }

    public static <A extends Comparable<A>> List<A> sort(Iterable<A> as) {
        return Sort.<A>sort().apply(as);
    }
}
