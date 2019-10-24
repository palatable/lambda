package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.builtin.fn2.CmpEq;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import static com.jnape.palatable.lambda.functions.builtin.fn3.CmpEqWith.cmpEqWith;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;
import static java.util.Comparator.comparing;

/**
 * Given a mapping function from some type <code>A</code> to some {@link Comparable} type <code>B</code> and two values
 * of type <code>A</code>, return <code>true</code> if the first value is strictly equal to the second value (according
 * to {@link Comparable#compareTo(Object)} in terms of their mapped <code>B</code> results; otherwise, return false.
 *
 * @param <A> the value type
 * @param <B> the mapped comparison type
 * @see CmpEq
 * @see CmpEqWith
 * @see LTBy
 * @see GTBy
 */
public final class CmpEqBy<A, B extends Comparable<B>> implements Fn3<Fn1<? super A, ? extends B>, A, A, Boolean> {

    private static final CmpEqBy<?, ?> INSTANCE = new CmpEqBy<>();

    private CmpEqBy() {
    }

    @Override
    public Boolean checkedApply(Fn1<? super A, ? extends B> compareFn, A x, A y) {
        return cmpEqWith(comparing(compareFn.toFunction()), x, y);
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
    public static <A, B extends Comparable<B>> CmpEqBy<A, B> cmpEqBy() {
        return (CmpEqBy<A, B>) INSTANCE;
    }

    public static <A, B extends Comparable<B>> BiPredicate<A, A> cmpEqBy(Fn1<? super A, ? extends B> compareFn) {
        return CmpEqBy.<A, B>cmpEqBy().apply(compareFn);
    }

    public static <A, B extends Comparable<B>> Predicate<A> cmpEqBy(Fn1<? super A, ? extends B> compareFn, A x) {
        return CmpEqBy.<A, B>cmpEqBy(compareFn).apply(x);
    }

    public static <A, B extends Comparable<B>> Boolean cmpEqBy(Fn1<? super A, ? extends B> compareFn, A x, A y) {
        return cmpEqBy(compareFn, x).apply(y);
    }
}
