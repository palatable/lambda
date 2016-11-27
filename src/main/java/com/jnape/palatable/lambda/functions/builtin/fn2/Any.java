package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;

import java.util.function.Function;

/**
 * Eagerly apply a predicate to each element in an <code>Iterable</code>, returning <code>true</code> if any element
 * satisfies the predicate, and <code>false</code> otherwise. This method short-circuits on the first <code>true</code>
 * evaluation.
 *
 * @param <A> The input Iterable element type
 * @see All
 */
public final class Any<A> implements BiPredicate<Function<? super A, Boolean>, Iterable<A>> {

    private Any() {
    }

    @Override
    public Boolean apply(Function<? super A, Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (predicate.apply(a))
                return true;

        return false;
    }

    public static <A> Any<A> any() {
        return new Any<>();
    }

    public static <A> Fn1<Iterable<A>, Boolean> any(Function<? super A, Boolean> predicate) {
        return Any.<A>any().apply(predicate);
    }

    public static <A> Boolean any(Function<? super A, Boolean> predicate, Iterable<A> as) {
        return Any.<A>any(predicate).apply(as);
    }
}
