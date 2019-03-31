package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqBy.cmpEqBy;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the second value is greater than or equal to the first value in
 * terms of their mapped <code>B</code> results according to {@link Comparable#compareTo(Object)}; otherwise, return
 * false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see GTE
 * @see LTEBy
 */
public final class GTEBy<A, B extends Comparable<B>> implements Fn3<Function<? super A, ? extends B>, A, A, Boolean> {

    private static final GTEBy INSTANCE = new GTEBy();

    private GTEBy() {
    }

    @Override
    public Boolean apply(Function<? super A, ? extends B> compareFn, A y, A x) {
        return GTBy.<A, B>gtBy(compareFn).or(cmpEqBy(compareFn)).apply(y, x);
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
    public static <A, B extends Comparable<B>> GTEBy<A, B> gteBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> gteBy(Function<? super A, ? extends B> fn) {
        return GTEBy.<A, B>gteBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> gteBy(Function<? super A, ? extends B> fn, A y) {
        return GTEBy.<A, B>gteBy(fn).apply(y);
    }

    public static <A, B extends Comparable<B>> Boolean gteBy(Function<? super A, ? extends B> fn, A y, A x) {
        return gteBy(fn, y).apply(x);
    }
}
