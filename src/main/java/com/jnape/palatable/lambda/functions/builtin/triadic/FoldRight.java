package com.jnape.palatable.lambda.functions.builtin.triadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functions.TriadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.monadic.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.triadic.FoldLeft.foldLeft;

public final class FoldRight<A, B> extends TriadicFunction<DyadicFunction<? super A, ? super B, ? extends B>, B, Iterable<A>, B> {

    @Override
    public final B apply(DyadicFunction<? super A, ? super B, ? extends B> function, B initialAccumulation,
                         Iterable<A> as) {
        return foldLeft(function.flip(), initialAccumulation, reverse(as));
    }

    public static <A, B> FoldRight<A, B> foldRight() {
        return new FoldRight<A, B>();
    }

    public static <A, B> DyadicFunction<B, Iterable<A>, B> foldRight(
            DyadicFunction<? super A, ? super B, ? extends B> function) {
        return FoldRight.<A, B>foldRight().partial(function);
    }

    public static <A, B> MonadicFunction<Iterable<A>, B> foldRight(
            DyadicFunction<? super A, ? super B, ? extends B> function,
            B initialAccumulation) {
        return foldRight(function).partial(initialAccumulation);
    }

    public static <A, B> B foldRight(DyadicFunction<? super A, ? super B, ? extends B> function, B initialAccumulation,
                                     Iterable<A> as) {
        return foldRight(function, initialAccumulation).apply(as);
    }
}
