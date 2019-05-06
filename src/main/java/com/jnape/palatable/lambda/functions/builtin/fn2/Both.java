package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn3;

import java.util.function.Function;

/**
 * Given two functions <code>f</code> and <code>g</code>, produce a
 * <code>{@link Fn1}&lt;A, {@link Tuple2}&lt;B, C&gt;&gt;</code> (the dual application of both functions).
 *
 * @param <A> both function's input type
 * @param <B> the first function return type
 * @param <C> the second function return type
 */
public final class Both<A, B, C> implements Fn3<Function<? super A, ? extends B>, Function<? super A, ? extends C>, A, Tuple2<B, C>> {

    private static final Both<?, ?, ?> INSTANCE = new Both<>();

    private Both() {
    }

    @Override
    public Tuple2<B, C> checkedApply(Function<? super A, ? extends B> f,
                                     Function<? super A, ? extends C> g,
                                     A a) {
        return Tuple2.fill(a).biMap(f, g);
    }

    @SuppressWarnings("unchecked")
    public static <A, B, C> Both<A, B, C> both() {
        return (Both<A, B, C>) INSTANCE;
    }

    public static <A, B, C> Fn1<Function<? super A, ? extends C>, Fn1<A, Tuple2<B, C>>> both(
            Function<? super A, ? extends B> f) {
        return Both.<A, B, C>both().apply(f);
    }

    public static <A, B, C> Fn1<A, Tuple2<B, C>> both(Function<? super A, ? extends B> f,
                                                      Function<? super A, ? extends C> g) {
        return Both.<A, B, C>both(f).apply(g);
    }

    public static <A, B, C> Tuple2<B, C> both(Function<? super A, ? extends B> f,
                                              Function<? super A, ? extends C> g,
                                              A a) {
        return Both.<A, B, C>both(f, g).apply(a);
    }
}
