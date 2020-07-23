package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;

/**
 * Function application, represented as a higher-order {@link Fn2} that receives an {@link Fn1} and its argument, and
 * applies it. Useful for treating application as a combinator, e.g.:
 * <pre>
 * {@code
 * List<Fn1<Integer, Integer>> fns     = asList(x -> x + 1, x -> x, x -> x - 1);
 * List<Integer>               args    = asList(0, 1, 2);
 * Iterable<Integer>           results = zipWith($(), fns, args); // [1, 1, 1]
 * }
 * </pre>
 *
 * @param <A> the applied {@link Fn1 Fn1's} input type
 * @param <B> the applied {@link Fn1 Fn1's} output type
 */
public final class $<A, B> implements Fn2<Fn1<? super A, ? extends B>, A, B> {
    private static final $<?, ?> INSTANCE = new $<>();

    private $() {
    }

    @Override
    public B checkedApply(Fn1<? super A, ? extends B> fn, A a) {
        return fn.apply(a);
    }

    @SuppressWarnings("unchecked")
    public static <A, B> $<A, B> $() {
        return ($<A, B>) INSTANCE;
    }

    public static <A, B> Fn1<A, B> $(Fn1<? super A, ? extends B> fn) {
        return $.<A, B>$().apply(fn);
    }

    public static <A, B> B $(Fn1<? super A, ? extends B> fn, A a) {
        return $.<A, B>$(fn).apply(a);
    }
}
