package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;

public final class FoldLeft<A, B> implements TriadicFunction<DyadicFunction<? super B, ? super A, ? extends B>, B, Iterable<A>, B> {

    @Override
    public final B apply(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                         Iterable<A> as) {
        B accumulation = initialAccumulation;
        for (A a : as)
            accumulation = function.apply(accumulation, a);
        return accumulation;
    }

    public static <A, B> FoldLeft<A, B> foldLeft() {
        return new FoldLeft<>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function) {
        return FoldLeft.<A, B>foldLeft().apply(function);
    }

    public static <A, B> MonadicFunction<Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function,
            B initialAccumulation) {
        return foldLeft(function).apply(initialAccumulation);
    }

    public static <A, B> B foldLeft(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                                    Iterable<A> as) {
        return foldLeft(function, initialAccumulation).apply(as);
    }
}
