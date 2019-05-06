package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.builtin.fn1.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;

/**
 * Given an {@link Iterable} and a {@link java.util.Comparator} over the {@link Iterable} element type, produce a
 * sorted {@link List} of the original elements based on sorting applied by the {@link java.util.Comparator}. Note that
 * this is both eager and monolithic.
 *
 * @param <A> the input Iterable and output List element type
 * @see Sort
 * @see SortBy
 */
public final class SortWith<A> implements Fn2<Comparator<? super A>, Iterable<A>, List<A>> {

    private static final SortWith<?> INSTANCE = new SortWith<>();

    private SortWith() {
    }

    @Override
    public List<A> checkedApply(Comparator<? super A> comparator, Iterable<A> as) {
        List<A> result = toCollection(ArrayList::new, as);
        result.sort(comparator);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <A> SortWith<A> sortWith() {
        return (SortWith<A>) INSTANCE;
    }

    public static <A> Fn1<Iterable<A>, List<A>> sortWith(Comparator<? super A> comparator) {
        return SortWith.<A>sortWith().apply(comparator);
    }

    public static <A> List<A> sortWith(Comparator<? super A> comparator, Iterable<A> as) {
        return SortWith.<A>sortWith(comparator).apply(as);
    }
}
