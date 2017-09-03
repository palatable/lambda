package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.SortBy.sortBy;

/**
 * Given an {@link Iterable} of {@link Comparable} elements, return a {@link List} of the sorted elements. Note that
 * this is both eager and monolithic.
 *
 * @param <A> the input Iterable and output List element type
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.SortBy
 */
public final class Sort<A extends Comparable<A>> implements Fn1<Iterable<A>, List<A>> {

    private static final Sort INSTANCE = new Sort();

    private Sort() {
    }

    @Override
    public List<A> apply(Iterable<A> as) {
        return sortBy(id(), as);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> Sort<A> sort() {
        return INSTANCE;
    }

    public static <A extends Comparable<A>> List<A> sort(Iterable<A> as) {
        return Sort.<A>sort().apply(as);
    }
}
