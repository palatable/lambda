package com.jnape.palatable.lambda.functions.builtin.fn1;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.HashMap;
import java.util.Map;

import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

/**
 * Given an <code>{@link Iterable}&lt;A&gt;</code>, return a <code>{@link Map}&lt;A, Long&gt;</code> representing each
 * unique element in the {@link Iterable} paired with its number of occurrences.
 *
 * @param <A> the {@link Iterable} element type
 */
public final class Occurrences<A> implements Fn1<Iterable<A>, Map<A, Long>> {
    private static final Occurrences<?> INSTANCE = new Occurrences<>();

    private Occurrences() {
    }

    @Override
    public Map<A, Long> checkedApply(Iterable<A> as) {
        return foldLeft((occurrences, a) -> {
            occurrences.put(a, occurrences.getOrDefault(a, 0L) + 1);
            return occurrences;
        }, new HashMap<>(), as);
    }

    @SuppressWarnings("unchecked")
    public static <A> Occurrences<A> occurrences() {
        return (Occurrences<A>) INSTANCE;
    }

    public static <A> Map<A, Long> occurrences(Iterable<A> as) {
        return Occurrences.<A>occurrences().apply(as);
    }
}
