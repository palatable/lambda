package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;

/**
 * Partially apply (fix) the first argument of a <code>TriadicFunction</code>, producing a <code>DyadicFunction</code>
 * that takes the remaining two argument. This is isomorphic to calling {@link com.jnape.palatable.lambda.functions.TriadicFunction#apply(Object)}.
 *
 * @param <A> The type of the value to be supplied
 * @param <B> The first input argument type of the resulting function
 * @param <C> The second input argument type of the resulting function
 * @param <D> The return type of the resulting function
 * @see Partial2
 */
public final class Partial3<A, B, C, D> implements DyadicFunction<TriadicFunction<A, B, C, D>, A, DyadicFunction<B, C, D>> {

    private Partial3() {
    }

    @Override
    public DyadicFunction<B, C, D> apply(TriadicFunction<A, B, C, D> function, A a) {
        return function.apply(a);
    }

    public static <A, B, C, D> Partial3<A, B, C, D> partial3() {
        return new Partial3<>();
    }

    public static <A, B, C, D> MonadicFunction<A, DyadicFunction<B, C, D>> partial3(
            TriadicFunction<A, B, C, D> function) {
        return Partial3.<A, B, C, D>partial3().apply(function);
    }

    public static <A, B, C, D> DyadicFunction<B, C, D> partial3(TriadicFunction<A, B, C, D> function, A a) {
        return partial3(function).apply(a);
    }
}
