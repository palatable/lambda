package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.DyadicFunction;
import com.jnape.palatable.lambda.MonadicFunction;

import static com.jnape.palatable.lambda.functions.ReduceLeft.reduceLeft;
import static com.jnape.palatable.lambda.functions.Reverse.reverse;

public final class ReduceRight<A> extends DyadicFunction<DyadicFunction<A, A, A>, Iterable<A>, A> {

    @Override
    public final A apply(DyadicFunction<A, A, A> function, Iterable<A> as) {
        return reduceLeft(function.flip(), reverse(as));
    }

    public static <A> ReduceRight<A> reduceRight() {
        return new ReduceRight<A>();
    }

    public static <A> MonadicFunction<Iterable<A>, A> reduceRight(DyadicFunction<A, A, A> function) {
        return ReduceRight.<A>reduceRight().partial(function);
    }

    public static <A> A reduceRight(DyadicFunction<A, A, A> function, Iterable<A> as) {
        return reduceRight(function).apply(as);
    }
}
