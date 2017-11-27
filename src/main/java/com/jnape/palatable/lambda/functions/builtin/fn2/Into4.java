package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn4;

/**
 * Given an <code>{@link Fn4}&lt;A, B, C, D, E&gt;</code> and a <code>{@link Tuple4}&lt;A, B, C, D&gt;</code>,
 * destructure the tuple and apply the slots as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the result type
 */
public final class Into4<A, B, C, D, E> implements Fn2<Fn4<A, B, C, D, E>, Tuple4<A, B, C, D>, E> {

    private static final Into4 INSTANCE = new Into4();

    @Override
    public E apply(Fn4<A, B, C, D, E> fn, Tuple4<A, B, C, D> tuple) {
        return tuple.into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E> Into4<A, B, C, D, E> into4() {
        return INSTANCE;
    }

    public static <A, B, C, D, E> Fn1<Tuple4<A, B, C, D>, E> into4(Fn4<A, B, C, D, E> fn) {
        return Into4.<A, B, C, D, E>into4().apply(fn);
    }

    public static <A, B, C, D, E> E into4(Fn4<A, B, C, D, E> fn, Tuple4<A, B, C, D> tuple) {
        return into4(fn).apply(tuple);
    }
}
