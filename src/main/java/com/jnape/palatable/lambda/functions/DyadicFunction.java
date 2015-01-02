package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.tuples.Tuple2;

@FunctionalInterface
public interface DyadicFunction<A, B, C> extends MonadicFunction<A, MonadicFunction<B, C>> {

    C apply(A a, B b);

    @Override
    default MonadicFunction<B, C> apply(A a) {
        return (b) -> apply(a, b);
    }

    default DyadicFunction<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    default MonadicFunction<Tuple2<A, B>, C> uncurry() {
        return (ab) -> apply(ab._1, ab._2);
    }
}
