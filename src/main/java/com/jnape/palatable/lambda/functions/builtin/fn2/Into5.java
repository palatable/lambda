package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn5;

/**
 * Given an <code>{@link Fn5}&lt;A, B, C, D, E, F&gt;</code> and a <code>{@link Product5}&lt;A, B, C, D, E&gt;</code>,
 * destructure the product and apply the slots as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the fifth argument type
 * @param <F> the result type
 */
public final class Into5<A, B, C, D, E, F> implements Fn2<Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F>, Product5<A, B, C, D, E>, F> {

    private static final Into5<?, ?, ?, ?, ?, ?> INSTANCE = new Into5<>();

    @Override
    public F checkedApply(Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> fn,
                          Product5<A, B, C, D, E> product) {
        return product.<F>into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E, F> Into5<A, B, C, D, E, F> into5() {
        return (Into5<A, B, C, D, E, F>) INSTANCE;
    }

    public static <A, B, C, D, E, F> Fn1<Product5<A, B, C, D, E>, F> into5(
            Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> fn) {
        return Into5.<A, B, C, D, E, F>into5().apply(fn);
    }

    public static <A, B, C, D, E, F> F into5(Fn5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> fn,
                                             Product5<A, B, C, D, E> product) {
        return Into5.<A, B, C, D, E, F>into5(fn).apply(product);
    }
}
