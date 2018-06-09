package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.CmpEq;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.function.Function;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the first value is strictly equal to the second value (according
 * to {@link Comparable#compareTo(Object)} in terms of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see CmpEq
 * @see LTBy
 * @see GTBy
 */
public final class CmpEqBy<A, B extends Comparable<B>> implements Fn3<Function<? super A, ? extends B>, A, A, Boolean> {

    private static final CmpEqBy INSTANCE = new CmpEqBy();

    private CmpEqBy() {
    }

    @Override
    public Boolean apply(Function<? super A, ? extends B> compareFn, A x, A y) {
        return compareFn.apply(x).compareTo(compareFn.apply(y)) == 0;
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
    public static <A, B extends Comparable<B>> CmpEqBy<A, B> cmpEqBy() {
        return INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> cmpEqBy(Function<? super A, ? extends B> compareFn) {
        return CmpEqBy.<A, B>cmpEqBy().apply(compareFn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> cmpEqBy(Function<? super A, ? extends B> compareFn, A x) {
        return CmpEqBy.<A, B>cmpEqBy(compareFn).apply(x);
    }

    public static <A, B extends Comparable<B>> Boolean cmpEqBy(Function<? super A, ? extends B> compareFn, A x, A y) {
        return cmpEqBy(compareFn, x).apply(y);
    }
}
