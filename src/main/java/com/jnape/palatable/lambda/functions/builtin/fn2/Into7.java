package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn7;

/**
 * Given an <code>{@link Fn7}&lt;A, B, C, D, E, F, G, H&gt;</code> and a
 * <code>{@link Tuple7}&lt;A, B, C, D, E, F, G&gt;</code>, destructure the tuple and apply the slots as arguments to the
 * function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the fifth argument type
 * @param <F> the sixth argument type
 * @param <G> the seventh argument type
 * @param <H> the result type
 */
public final class Into7<A, B, C, D, E, F, G, H> implements Fn2<Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H>, Tuple7<A, B, C, D, E, F, G>, H> {

    private static final Into7 INSTANCE = new Into7();

    @Override
    public H apply(Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn,
                   Tuple7<A, B, C, D, E, F, G> tuple) {
        return tuple.into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, H> Into7<A, B, C, D, E, F, G, H> into7() {
        return INSTANCE;
    }

    public static <A, B, C, D, E, F, G, H> Fn1<Tuple7<A, B, C, D, E, F, G>, H> into7(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn) {
        return Into7.<A, B, C, D, E, F, G, H>into7().apply(fn);
    }

    public static <A, B, C, D, E, F, G, H> H into7(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn,
            Tuple7<A, B, C, D, E, F, G> tuple) {
        return Into7.<A, B, C, D, E, F, G, H>into7(fn).apply(tuple);
    }
}
