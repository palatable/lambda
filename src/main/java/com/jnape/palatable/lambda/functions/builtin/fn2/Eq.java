package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.BiPredicate;

/**
 * Type-safe equality in function form; uses {@link Object#equals}, not <code>==</code>.
 *
 * @param <A> the type to compare for equality
 */
public final class Eq<A> implements BiPredicate<A, A> {

    private Eq() {
    }

    @Override
    public Boolean apply(A x, A y) {
        return x == null ? y == null : x.equals(y);
    }

    public static <A> Eq<A> eq() {
        return new Eq<>();
    }

    public static <A> Fn1<A, Boolean> eq(A x) {
        return Eq.<A>eq().apply(x);
    }

    public static <A> Boolean eq(A x, A y) {
        return eq(x).apply(y);
    }
}
