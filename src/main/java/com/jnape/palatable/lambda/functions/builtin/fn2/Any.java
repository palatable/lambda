package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

/**
 * Eagerly apply a predicate to each element in an <code>Iterable</code>, returning <code>true</code> if any element
 * satisfies the predicate, and <code>false</code> otherwise. This method short-circuits on the first <code>true</code>
 * evaluation.
 *
 * @param <A> The input Iterable element type
 * @see All
 */
public final class Any<A> implements BiPredicate<Fn1<? super A, ? extends Boolean>, Iterable<A>> {

    private static final Any<?> INSTANCE = new Any<>();

    private Any() {
    }

    @Override
    public Boolean checkedApply(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (predicate.apply(a))
                return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <A> Any<A> any() {
        return (Any<A>) INSTANCE;
    }

    public static <A> Predicate<Iterable<A>> any(Fn1<? super A, ? extends Boolean> predicate) {
        return Any.<A>any().apply(predicate);
    }

    public static <A> Boolean any(Fn1<? super A, ? extends Boolean> predicate, Iterable<A> as) {
        return Any.<A>any(predicate).apply(as);
    }
}
