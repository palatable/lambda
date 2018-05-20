package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.function.BiFunction;

/**
 * Given a <code>{@link BiFunction}&lt;A, B, C&gt;</code> and a <code>{@link Product2}&lt;A, B&gt;</code>, destructure
 * the product and apply the slots as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the result type
 */
public final class Into<A, B, C> implements Fn2<BiFunction<? super A, ? super B, ? extends C>, Product2<A, B>, C> {

    private static final Into INSTANCE = new Into();

    private Into() {
    }

    @Override
    public C apply(BiFunction<? super A, ? super B, ? extends C> fn, Product2<A, B> product) {
        return product.into(fn);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> Into<A, B, C> into() {
        return INSTANCE;
    }

    public static <A, B, C> Fn1<Product2<A, B>, C> into(
            BiFunction<? super A, ? super B, ? extends C> fn) {
        return Into.<A, B, C>into().apply(fn);
    }

    public static <A, B, C> C into(BiFunction<? super A, ? super B, ? extends C> fn,
                                   Product2<A, B> product) {
        return Into.<A, B, C>into(fn).apply(product);
    }
}
