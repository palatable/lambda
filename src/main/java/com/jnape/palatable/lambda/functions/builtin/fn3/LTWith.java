package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.LT;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Compare.compare;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.lessThan;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a comparator for some type <code>A</code> and two values of type <code>A</code>,
 * return <code>true</code> if the second value is strictly less than than the first value in
 * terms of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @see LT
 * @see LTBy
 * @see GTWith
 */
public final class LTWith<A> implements Fn3<Comparator<A>, A, A, Boolean> {

    private static final LTWith<?> INSTANCE = new LTWith<>();

    private LTWith() {
    }

    @Override
    public BiPredicate<A, A> apply(Comparator<A> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Comparator<A> compareFn, A x) {
        return predicate(Fn3.super.apply(compareFn, x));
    }

    @SuppressWarnings("unchecked")
    public static <A> LTWith<A> ltWith() {
        return (LTWith<A>) INSTANCE;
    }

    public static <A> BiPredicate<A, A> ltWith(Comparator<A> comparator) {
        return LTWith.<A>ltWith().apply(comparator);
    }

    public static <A> Predicate<A> ltWith(Comparator<A> comparator, A y) {
        return LTWith.ltWith(comparator).apply(y);
    }

    public static <A> Boolean ltWith(Comparator<A> comparator, A y, A x) {
        return ltWith(comparator, y).apply(x);
    }

    @Override
    public Boolean checkedApply(Comparator<A> comparator, A a, A a2) {
        return compare(comparator, a, a2).equals(lessThan());
    }
}
