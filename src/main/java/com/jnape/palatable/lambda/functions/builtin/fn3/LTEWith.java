package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.LTE;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.GTWith.gtWith;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a {@link Comparator} from some type <code>A</code> and two values of type <code>A</code>,
 * return <code>true</code> if the second value is less than or equal to the first value in
 * terms of their mapped <code>B</code> results according to {@link Comparator#compare(A, A)};
 * otherwise, return false.
 *
 * @param <A> the value type
 * @see LTE
 * @see LTEBy
 * @see GTEWith
 */
public final class LTEWith<A> implements Fn3<Comparator<A>, A, A, Boolean> {

    private static final LTEWith<?> INSTANCE = new LTEWith<>();

    private LTEWith() {
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
    public static <A> LTEWith<A> lteWith() {
        return (LTEWith<A>) INSTANCE;
    }

    public static <A> BiPredicate<A, A> lteWith(Comparator<A> comparator) {
        return LTEWith.<A>lteWith().apply(comparator);
    }

    public static <A> Predicate<A> lteWith(Comparator<A> comparator, A y) {
        return LTEWith.lteWith(comparator).apply(y);
    }

    public static <A> Boolean lteWith(Comparator<A> comparator, A y, A x) {
        return lteWith(comparator, y).apply(x);
    }

    @Override
    public Boolean checkedApply(Comparator<A> comparator, A a, A a2) {
        return !gtWith(comparator, a, a2);
    }
}
