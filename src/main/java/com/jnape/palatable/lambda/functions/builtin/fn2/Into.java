package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Given a <code>{@link BiFunction}&lt;A, B, C&gt;</code> and a <code>{@link Map.Entry}&lt;A, B&gt;</code>, destructure
 * the entry and apply the key and value as arguments to the function, returning the result.
 *
 * @param <A> the first argument type
 * @param <B> the second argument type
 * @param <C> the result type
 */
public final class Into<A, B, C> implements Fn2<BiFunction<? super A, ? super B, ? extends C>, Map.Entry<A, B>, C> {

    private static final Into<?, ?, ?> INSTANCE = new Into<>();

    private Into() {
    }

    @Override
    public C checkedApply(BiFunction<? super A, ? super B, ? extends C> fn, Map.Entry<A, B> entry) {
        return fn.apply(entry.getKey(), entry.getValue());
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> Into<A, B, C> into() {
        return (Into<A, B, C>) INSTANCE;
    }

    public static <A, B, C> Fn1<Map.Entry<A, B>, C> into(
            BiFunction<? super A, ? super B, ? extends C> fn) {
        return Into.<A, B, C>into().apply(fn);
    }

    public static <A, B, C> C into(BiFunction<? super A, ? super B, ? extends C> fn,
                                   Map.Entry<A, B> entry) {
        return Into.<A, B, C>into(fn).apply(entry);
    }
}
