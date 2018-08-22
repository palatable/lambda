package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqBy.cmpEqBy;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the first value is less than or equal to the second value in
 * terms of their mapped <code>B</code> results according to {@link Comparable#compareTo(Object)}; otherwise, return
 * false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see LTE
 * @see GTEBy
 */
public final class LTEBy<A, B extends Comparable<B>> implements Fn3<Function<? super A, ? extends B>, A, A, Boolean> {

    private static final LTEBy INSTANCE = new LTEBy();

    private LTEBy() {
    }

    @Override
    public Boolean apply(Function<? super A, ? extends B> compareFn, A y, A x) {
        return LTBy.<A, B>ltBy(compareFn).or(cmpEqBy(compareFn)).apply(y, x);
    }

    @Override
    public BiPredicate<A, A> apply(Function<? super A, ? extends B> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Function<? super A, ? extends B> compareFn, A y) {
        return Fn3.super.apply(compareFn, y)::apply;
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> LTEBy<A, B> lteBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> lteBy(Function<? super A, ? extends B> fn) {
        return LTEBy.<A, B>lteBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> lteBy(Function<? super A, ? extends B> fn, A y) {
        return LTEBy.<A, B>lteBy(fn).apply(y);
    }

    public static <A, B extends Comparable<B>> Boolean lteBy(Function<? super A, ? extends B> fn, A y, A x) {
        return lteBy(fn, y).apply(x);
    }
}
