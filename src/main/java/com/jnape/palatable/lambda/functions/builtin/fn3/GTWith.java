package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.GT;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Compare.compare;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.greaterThan;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a {@link Comparator} from some type <code>A</code> and two values of type <code>A</code>,
 * return <code>true</code> if the second value is strictly greater than the first value in
 * terms of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @see GT
 * @see GTBy
 * @see LTWith
 */
public final class GTWith<A> implements Fn3<Comparator<A>, A, A, Boolean> {

    private static final GTWith<?> INSTANCE = new GTWith<>();

    private GTWith() {
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
    public static <A> GTWith<A> gtWith() {
        return (GTWith<A>) INSTANCE;
    }

    public static <A> BiPredicate<A, A> gtWith(Comparator<A> comparator) {
        return GTWith.<A>gtWith().apply(comparator);
    }

    public static <A> Predicate<A> gtWith(Comparator<A> comparator, A y) {
        return GTWith.gtWith(comparator).apply(y);
    }

    public static <A> Boolean gtWith(Comparator<A> comparator, A y, A x) {
        return gtWith(comparator, y).apply(x);
    }

    @Override
    public Boolean checkedApply(Comparator<A> comparator, A a, A a2) {
        return compare(comparator, a, a2).equals(greaterThan());
    }
}
