package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.GTE;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.LTWith.ltWith;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a {@link Comparator} from some type <code>A</code> and two values of type <code>A</code>,
 * return <code>true</code> if the second value is greater than or equal to the first value in
 * terms of their mapped <code>B</code> results according to {@link Comparator#compare(Object, Object)};
 * otherwise, return false.
 *
 * @param <A> the value type
 * @see GTE
 * @see GTEBy
 * @see LTEWith
 */
public final class GTEWith<A> implements Fn3<Comparator<A>, A, A, Boolean> {

    private static final GTEWith<?> INSTANCE = new GTEWith<>();

    private GTEWith() {
    }

    @Override
    public BiPredicate<A, A> apply(Comparator<A> compareFn) {
        return Fn3.super.apply(compareFn)::apply;
    }

    @Override
    public Predicate<A> apply(Comparator<A> compareFn, A x) {
        return predicate(Fn3.super.apply(compareFn, x));
    }

    @Override
    public Boolean checkedApply(Comparator<A> comparator, A a, A a2) {
        return !ltWith(comparator, a, a2);
    }

    @SuppressWarnings("unchecked")
    public static <A> GTEWith<A> gteWith() {
        return (GTEWith<A>) INSTANCE;
    }

    public static <A> BiPredicate<A, A> gteWith(Comparator<A> comparator) {
        return GTEWith.<A>gteWith().apply(comparator);
    }

    public static <A> Predicate<A> gteWith(Comparator<A> comparator, A y) {
        return GTEWith.gteWith(comparator).apply(y);
    }

    public static <A> Boolean gteWith(Comparator<A> comparator, A y, A x) {
        return gteWith(comparator, y).apply(x);
    }
}
