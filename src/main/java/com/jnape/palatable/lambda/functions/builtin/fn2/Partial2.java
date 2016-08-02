package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.BiFunction;

/**
 * Partially apply (fix) the first argument of a <code>{@link BiFunction}</code>, producing an <code>Fn1</code> that
 * takes the remaining argument. This is isomorphic to calling {@link Fn2#apply(Object)}.
 *
 * @param <A> The type of the value to be supplied
 * @param <B> The input argument type of the resulting function
 * @param <C> The return type of the resulting function
 * @see Partial3
 */
public final class Partial2<A, B, C> implements Fn2<BiFunction<A, B, C>, A, Fn1<B, C>> {

    private Partial2() {
    }

    @Override
    public Fn1<B, C> apply(BiFunction<A, B, C> fn, A a) {
        return b -> fn.apply(a, b);
    }

    public static <A, B, C> Partial2<BiFunction<A, B, C>, A, Fn1<B, C>> partial2() {
        return new Partial2<>();
    }

    public static <A, B, C> Fn1<A, Fn1<B, C>> partial2(BiFunction<A, B, C> fn) {
        return Partial2.<A, B, C>partial2().apply(new Partial2<A, B, C>().toBiFunction(), fn);
    }

    public static <A, B, C> Fn1<B, C> partial2(BiFunction<A, B, C> fn, A a) {
        return partial2(fn).apply(a);
    }
}
