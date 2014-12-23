package com.jnape.palatable.lambda.functions.builtin.dyadic;

import com.jnape.palatable.lambda.functions.DyadicFunction;
import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.ReduceLeft.reduceLeft;
import static com.jnape.palatable.lambda.functions.builtin.monadic.Reverse.reverse;

public final class ReduceRight<A> implements DyadicFunction<DyadicFunction<? super A, ? super A, ? extends A>, Iterable<A>, A> {

    @Override
    public final A apply(DyadicFunction<? super A, ? super A, ? extends A> function, Iterable<A> as) {
        return reduceLeft(function.flip(), reverse(as));
    }

    public static <A> ReduceRight<A> reduceRight() {
        return new ReduceRight<>();
    }

    public static <A> MonadicFunction<Iterable<A>, A> reduceRight(
            DyadicFunction<? super A, ? super A, ? extends A> function) {
        return ReduceRight.<A>reduceRight().apply(function);
    }

    public static <A> A reduceRight(DyadicFunction<? super A, ? super A, ? extends A> function, Iterable<A> as) {
        return reduceRight(function).apply(as);
    }
}
