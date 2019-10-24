package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Comparator;

import static com.jnape.palatable.lambda.functions.builtin.fn3.Compare.compare;
import static com.jnape.palatable.lambda.functions.ordering.ComparisonRelation.equal;
import static com.jnape.palatable.lambda.functions.specialized.Predicate.predicate;

/**
 * Given a {@link Comparator} from some type <code>A</code> and two values of type <code>A</code>, return <code>true</code>
 * if the first value is strictly equal to the second value (according to {@link Comparator#compare(A, A)}
 * otherwise, return false.
 *
 * @param <A> the value type
 * @see CmpEqBy
 * @see LTBy
 * @see GTBy
 * @see Compare
 */
public final class CmpEqWith<A> implements Fn3<Comparator<A>, A, A, Boolean> {

    private static final CmpEqWith<?> INSTANCE = new CmpEqWith<>();

    private CmpEqWith() {
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
    public static <A> CmpEqWith<A> cmpEqWith() {
        return (CmpEqWith<A>) INSTANCE;
    }

    public static <A> BiPredicate<A, A> cmpEqWith(Comparator<A> comparator) {
        return CmpEqWith.<A>cmpEqWith().apply(comparator);
    }

    public static <A> Predicate<A> cmpEqWith(Comparator<A> comparator, A x) {
        return CmpEqWith.cmpEqWith(comparator).apply(x);
    }

    public static <A> Boolean cmpEqWith(Comparator<A> comparator, A x, A y) {
        return cmpEqWith(comparator, x).apply(y);
    }

    @Override
    public Boolean checkedApply(Comparator<A> comparator, A a, A a2) {
        return compare(comparator, a, a2).equals(equal());
    }
}
