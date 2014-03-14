package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;
import com.jnape.palatable.lambda.TriadicFunction;

public final class FoldLeft<A, B> extends TriadicFunction<DyadicFunction<? super B, ? super A, ? extends B>, B, Iterable<A>, B> {

    @Override
    public final B apply(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                         Iterable<A> as) {
        B accumulation = initialAccumulation;
        for (A a : as)
            accumulation = function.apply(accumulation, a);
        return accumulation;
    }

    public static <A, B> FoldLeft<A, B> foldLeft() {
        return new FoldLeft<A, B>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function) {
        return FoldLeft.<A, B>foldLeft().partial(function);
    }

    public static <A, B> MonadicFunction<Iterable<A>, B> foldLeft(
            DyadicFunction<? super B, ? super A, ? extends B> function,
            B initialAccumulation) {
        return foldLeft(function).partial(initialAccumulation);
    }

    public static <A, B> B foldLeft(DyadicFunction<? super B, ? super A, ? extends B> function, B initialAccumulation,
                                    Iterable<A> as) {
        return foldLeft(function, initialAccumulation).apply(as);
    }
}
