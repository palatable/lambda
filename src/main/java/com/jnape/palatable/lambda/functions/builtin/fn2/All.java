package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;

import java.util.function.Function;

/**
 * Eagerly apply a predicate to each element in an <code>Iterable</code>, returning <code>true</code> if every element
 * satisfies the predicate, and <code>false</code> otherwise. This method short-circuits on the first <code>false</code>
 * evaluation.
 *
 * @param <A> The input Iterable element type
 * @see Any
 */
public final class All<A> implements BiPredicate<Function<? super A, Boolean>, Iterable<A>> {

    private All() {
    }

    @Override
    public Boolean apply(Function<? super A, Boolean> predicate, Iterable<A> as) {
        for (A a : as)
            if (!predicate.apply(a))
                return false;

        return true;
    }

    public static <A> All<A> all() {
        return new All<>();
    }

    public static <A> Fn1<Iterable<A>, Boolean> all(Function<? super A, Boolean> predicate) {
        return All.<A>all().apply(predicate);
    }

    public static <A> Boolean all(Function<? super A, Boolean> predicate, Iterable<A> as) {
        return All.<A>all(predicate).apply(as);
    }
}
