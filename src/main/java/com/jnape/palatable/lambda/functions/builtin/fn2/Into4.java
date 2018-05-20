package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn4;

/**
 * Given an <code>{@link Fn4}&lt;A, B, C, D, E&gt;</code> and a <code>{@link Product4}&lt;A, B, C, D&gt;</code>,
 * destructure the product and apply the slots as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the third argument type
 * @param <D> the fourth argument type
 * @param <E> the result type
 */
public final class Into4<A, B, C, D, E> implements Fn2<Fn4<? super A, ? super B, ? super C, ? super D, ? extends E>, Product4<A, B, C, D>, E> {

    private static final Into4 INSTANCE = new Into4();

    @Override
    public E apply(Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> fn, Product4<A, B, C, D> product) {
        return product.<E>into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C, D, E> Into4<A, B, C, D, E> into4() {
        return INSTANCE;
    }

    public static <A, B, C, D, E> Fn1<Product4<A, B, C, D>, E> into4(
            Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> fn) {
        return Into4.<A, B, C, D, E>into4().apply(fn);
    }

    public static <A, B, C, D, E> E into4(Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> fn,
                                          Product4<A, B, C, D> product) {
        return Into4.<A, B, C, D, E>into4(fn).apply(product);
    }
}
