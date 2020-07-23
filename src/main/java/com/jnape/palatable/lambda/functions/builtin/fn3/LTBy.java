package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn3.LTWith.ltWith;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;
import static java.util.Comparator.comparing;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the second value is strictly less than the first value in terms
 * of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see LT
 * @see GTBy
 */
public final class LTBy<A, B extends Comparable<B>> implements Fn3<Fn1<? super A, ? extends B>, A, A, Boolean> {

    private static final LTBy<?, ?> INSTANCE = new LTBy<>();

    private LTBy() {
    }

    @Override
    public Boolean checkedApply(Fn1<? super A, ? extends B> compareFn, A y, A x) {
        return ltWith(comparing(compareFn.toFunction()), y, x);
    }

    @Override
    public BiPredicate<A, A> apply(Fn1<? super A, ? extends B> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Fn1<? super A, ? extends B> compareFn, A y) {
        return predicate(Fn3.super.apply(compareFn, y));
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends Comparable<B>> LTBy<A, B> ltBy() {
        return (LTBy<A, B>) INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> ltBy(Fn1<? super A, ? extends B> fn) {
        return LTBy.<A, B>ltBy().apply(fn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> ltBy(Fn1<? super A, ? extends B> fn, A y) {
        return LTBy.<A, B>ltBy(fn).apply(y);
    }

    public static <A, B extends Comparable<B>> Boolean ltBy(Fn1<? super A, ? extends B> fn, A y, A x) {
        return ltBy(fn, y).apply(x);
    }
}
