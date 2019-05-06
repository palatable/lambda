package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;

/**
 * Partially apply (fix) the first argument of a <code>Fn3</code>, producing a <code>Fn2</code> that takes the remaining
 * two argument. This is isomorphic to calling {@link Fn3#apply(Object)}.
 *
 * @param <A> The type of the value to be supplied
 * @param <B> The first input argument type of the resulting function
 * @param <C> The second input argument type of the resulting function
 * @param <D> The return type of the resulting function
 * @see Partial2
 */
public final class Partial3<A, B, C, D> implements Fn2<Fn3<A, B, C, D>, A, Fn2<B, C, D>> {

    private static final Partial3<?, ?, ?, ?> INSTANCE = new Partial3<>();

    private Partial3() {
    }

    @Override
    public Fn2<B, C, D> checkedApply(Fn3<A, B, C, D> fn, A a) {
        return fn.apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D> Partial3<A, B, C, D> partial3() {
        return (Partial3<A, B, C, D>) INSTANCE;
    }

    public static <A, B, C, D> Fn1<A, Fn2<B, C, D>> partial3(Fn3<A, B, C, D> fn) {
        return Partial3.<A, B, C, D>partial3().apply(fn);
    }

    public static <A, B, C, D> Fn2<B, C, D> partial3(Fn3<A, B, C, D> fn, A a) {
        return partial3(fn).apply(a);
    }
}
