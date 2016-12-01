package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.monoid.Monoid;

@FunctionalInterface
public interface BiMonoidFactory<A, B, C> extends BiSemigroupFactory<A, B, C> {

    @Override
    default MonoidFactory<B, C> apply(A a) {
        return b -> apply(a, b);
    }

    @Override
    Monoid<C> apply(A a, B b);

    @Override
    default BiMonoidFactory<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    @Override
    default MonoidFactory<Tuple2<A, B>, C> uncurry() {
        return ab -> apply(ab._1()).apply(ab._2());
    }
}
