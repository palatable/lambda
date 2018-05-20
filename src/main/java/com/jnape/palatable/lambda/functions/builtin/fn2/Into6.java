package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn6;

/**
 * Given an <code>{@link Fn6}&lt;A, B, C, D, E, F, G&gt;</code> and a
 * <code>{@link Tuple6}&lt;A, B, C, D, E, F&gt;</code>, destructure the tuple and apply the slots as arguments to the
 * function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the fifth argument type
 * @param <F> the sixth argument type
 * @param <G> the result type
 */
public final class Into6<A, B, C, D, E, F, G> implements Fn2<Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends G>, Tuple6<A, B, C, D, E, F>, G> {

    private static final Into6 INSTANCE = new Into6();

    @Override
    public G apply(Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends G> fn,
                   Tuple6<A, B, C, D, E, F> tuple) {
        return tuple.<G>into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G> Into6<A, B, C, D, E, F, G> into6() {
        return INSTANCE;
    }

    public static <A, B, C, D, E, F, G> Fn1<Tuple6<A, B, C, D, E, F>, G> into6(
            Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends G> fn) {
        return Into6.<A, B, C, D, E, F, G>into6().apply(fn);
    }

    public static <A, B, C, D, E, F, G> G into6(
            Fn6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends G> fn,
            Tuple6<A, B, C, D, E, F> tuple) {
        return Into6.<A, B, C, D, E, F, G>into6(fn).apply(tuple);
    }
}
