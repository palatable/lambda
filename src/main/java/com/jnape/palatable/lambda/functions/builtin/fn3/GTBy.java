package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the second value is strictly greater than the first value in
 * terms of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see GT
 * @see LTBy
 */
public final class GTBy<A, B extends Comparable<B>> implements Fn3<Fn1<? super A, ? extends B>, A, A, Boolean> {

    private static final GTBy<?, ?> INSTANCE = new GTBy<>();

    private GTBy() {
    }

    @Override
    public Boolean checkedApply(Fn1<? super A, ? extends B> compareFn, A y, A x) {
        return compareFn.apply(x).compareTo(compareFn.apply(y)) > 0;
    }

    @Override
    public BiPredicate<A, A> apply(Fn1<? super A, ? extends B> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Fn1<? super A, ? extends B> compareFn, A x) {
        return predicate(Fn3.super.apply(compareFn, x));
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> GTBy<A, B> gtBy() {
        return (GTBy<A, B>) INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> gtBy(Fn1<? super A, ? extends B> fn) {
        return GTBy.<A, B>gtBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> gtBy(Fn1<? super A, ? extends B> fn, A y) {
        return GTBy.<A, B>gtBy(fn).apply(y);
    }

    public static <A, B extends Comparable<B>> Boolean gtBy(Fn1<? super A, ? extends B> fn, A y, A x) {
        return gtBy(fn, y).apply(x);
    }
}
