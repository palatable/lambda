package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.product.Product7;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn7;

/**
 * Given an <code>{@link Fn7}&lt;A, B, C, D, E, F, G, H&gt;</code> and a
 * <code>{@link Product7}&lt;A, B, C, D, E, F, G&gt;</code>, destructure the product and apply the slots as arguments to
 * the function, returning the result.
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
public final class Into7<A, B, C, D, E, F, G, H> implements Fn2<Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H>, Product7<A, B, C, D, E, F, G>, H> {

    private static final Into7<?, ?, ?, ?, ?, ?, ?, ?> INSTANCE = new Into7<>();

    @Override
    public H checkedApply(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn,
            Product7<A, B, C, D, E, F, G> product) {
        return product.<H>into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F, G, H> Into7<A, B, C, D, E, F, G, H> into7() {
        return (Into7<A, B, C, D, E, F, G, H>) INSTANCE;
    }

    public static <A, B, C, D, E, F, G, H> Fn1<Product7<A, B, C, D, E, F, G>, H> into7(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn) {
        return Into7.<A, B, C, D, E, F, G, H>into7().apply(fn);
    }

    public static <A, B, C, D, E, F, G, H> H into7(
            Fn7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends H> fn,
            Product7<A, B, C, D, E, F, G> product) {
        return Into7.<A, B, C, D, E, F, G, H>into7(fn).apply(product);
    }
}
