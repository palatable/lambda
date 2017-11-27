package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn8;

/**
 * Given an <code>{@link Fn8}&lt;A, B, C, D, E, F, G, H, I&gt;</code> and a
 * <code>{@link Tuple8}&lt;A, B, C, D, E, F, G, H&gt;</code>, destructure the tuple and apply the slots as arguments to
 * the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the fifth argument type
 * @param <F> the sixth argument type
 * @param <G> the seventh argument type
 * @param <H> the eighth argument type
 * @param <I> the result type
 */
public final class Into8<A, B, C, D, E, F, G, H, I> implements Fn2<Fn8<A, B, C, D, E, F, G, H, I>, Tuple8<A, B, C, D, E, F, G, H>, I> {

    private static final Into8 INSTANCE = new Into8();

    @Override
    public I apply(Fn8<A, B, C, D, E, F, G, H, I> fn, Tuple8<A, B, C, D, E, F, G, H> tuple) {
        return tuple.into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, H, I> Into8<A, B, C, D, E, F, G, H, I> into8() {
        return INSTANCE;
    }

    public static <A, B, C, D, E, F, G, H, I> Fn1<Tuple8<A, B, C, D, E, F, G, H>, I> into8(
            Fn8<A, B, C, D, E, F, G, H, I> fn) {
        return Into8.<A, B, C, D, E, F, G, H, I>into8().apply(fn);
    }

    public static <A, B, C, D, E, F, G, H, I> I into8(Fn8<A, B, C, D, E, F, G, H, I> fn,
                                                      Tuple8<A, B, C, D, E, F, G, H> tuple) {
        return into8(fn).apply(tuple);
    }
}
