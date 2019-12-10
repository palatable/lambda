package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.specialized.BiPredicate;
import com.jnape.palatable.lambda.functions.specialized.Predicate;

import java.util.Objects;

/**
 * Type-safe equality in function form; uses {@link Object#equals}, not <code>==</code>.
 *
 * @param <A> the type to compare for equality
 */
public final class Eq<A> implements BiPredicate<A, A> {

    private static final Eq<?> INSTANCE = new Eq<>();

    private Eq() {
    }

    @Override
    public Boolean checkedApply(A x, A y) {
        return Objects.equals(x, y);
    }

    @SuppressWarnings("unchecked")
    public static <A> Eq<A> eq() {
        return (Eq<A>) INSTANCE;
    }

    public static <A> Predicate<A> eq(A x) {
        return Eq.<A>eq().apply(x);
    }

    public static <A> Boolean eq(A x, A y) {
        return eq(x).apply(y);
    }
}
