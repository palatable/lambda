package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn5;

/**
 * Given an <code>{@link Fn5}&lt;A, B, C, D, E, F&gt;</code> and a <code>{@link Tuple5}&lt;A, B, C, D, E&gt;</code>,
 * destructure the tuple and apply the slots as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the fifth argument type
 * @param <F> the result type
 */
public final class Into5<A, B, C, D, E, F> implements Fn2<Fn5<A, B, C, D, E, F>, Tuple5<A, B, C, D, E>, F> {

    private static final Into5 INSTANCE = new Into5();

    @Override
    public F apply(Fn5<A, B, C, D, E, F> fn, Tuple5<A, B, C, D, E> tuple) {
        return tuple.into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F> Into5<A, B, C, D, E, F> into5() {
        return INSTANCE;
    }

    public static <A, B, C, D, E, F> Fn1<Tuple5<A, B, C, D, E>, F> into5(Fn5<A, B, C, D, E, F> fn) {
        return Into5.<A, B, C, D, E, F>into5().apply(fn);
    }

    public static <A, B, C, D, E, F> F into5(Fn5<A, B, C, D, E, F> fn, Tuple5<A, B, C, D, E> tuple) {
        return into5(fn).apply(tuple);
    }
}
