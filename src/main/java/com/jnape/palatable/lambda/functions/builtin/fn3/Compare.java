package com.jnape.palatable.lambda.functions.builtin.fn3;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.ordering.ComparisonRelation;

import java.util.Comparator;

/**
 * Given a {@link Comparator} from some type <code>A</code> and two values of type <code>A</code>, return a
 * {@link ComparisonRelation} of the first value with reference to the second value (according to {@link Comparator#compare(A, A)}.
 * The order of parameters is flipped with respect to {@link Comparator#compare(A, A)} for more idiomatic partial application.
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 *  Compare.compare(naturalOrder(), 1, 2); // ComparisonRelation.GreaterThan
 *  Compare.compare(naturalOrder(), 2, 1); // ComparisonRelation.LessThan
 *  Compare.compare(naturalOrder(), 1, 1); // ComparisonRelation.Equal
 * }
 * </pre>
 * </p>
 *
 * @param <A> the value type
 * @see Comparator
 * @see Compare
 */
public final class Compare<A> implements Fn3<Comparator<A>, A, A, ComparisonRelation> {
    private static final Compare<?> INSTANCE = new Compare<>();

    private Compare() { }

    @Override
    public ComparisonRelation checkedApply(Comparator<A> aComparator, A a, A a2) throws Throwable {
        return ComparisonRelation.fromInt(aComparator.compare(a2, a));
    }

    @SuppressWarnings("unchecked")
    public static <A> Compare<A> compare() {
        return (Compare<A>) INSTANCE;
    }

    public static <A> Fn2<A, A, ComparisonRelation> compare(Comparator<A> comparator) {
        return Compare.<A>compare().apply(comparator);
    }

    public static <A> Fn1<A, ComparisonRelation> compare(Comparator<A> comparator, A a) {
        return compare(comparator).apply(a);
    }

    public static <A> ComparisonRelation compare(Comparator<A> aComparator, A a, A a2) {
        return compare(aComparator, a).apply(a2);
    }
}
