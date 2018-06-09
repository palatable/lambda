package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.Function;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the first value is strictly less than the second value in terms
 * of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see LT
 * @see GTBy
 */
public final class LTBy<A, B extends Comparable<B>> implements Fn3<Function<? super A, ? extends B>, A, A, Boolean> {

    private static final LTBy INSTANCE = new LTBy();

    private LTBy() {
    }

    @Override
    public Boolean apply(Function<? super A, ? extends B> compareFn, A x, A y) {
        return compareFn.apply(x).compareTo(compareFn.apply(y)) < 0;
    }

    @Override
    public BiPredicate<A, A> apply(Function<? super A, ? extends B> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Function<? super A, ? extends B> compareFn, A x) {
        return Fn3.super.apply(compareFn, x)::apply;
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> LTBy<A, B> ltBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> ltBy(Function<? super A, ? extends B> fn) {
        return LTBy.<A, B>ltBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> ltBy(Function<? super A, ? extends B> fn, A x) {
        return LTBy.<A, B>ltBy(fn).apply(x);
    }

    public static <A, B extends Comparable<B>> Boolean ltBy(Function<? super A, ? extends B> fn, A x, A y) {
        return ltBy(fn, x).apply(y);
    }
}
