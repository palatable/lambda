package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.semigroup.Semigroup;

@FunctionalInterface
public interface BiSemigroupFactory<A, B, C> extends Fn4<A, B, C, C, C> {

    @Override
    Semigroup<C> apply(A a, B b);

    @Override
    default SemigroupFactory<B, C> apply(A a) {
        return b -> apply(a, b);
    }

    @Override
    default BiSemigroupFactory<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    @Override
    default SemigroupFactory<Tuple2<A, B>, C> uncurry() {
        return ab -> apply(ab._1(), ab._2());
    }

    @Override
    default C apply(A a, B b, C c, C d) {
        return apply(a).apply(b).apply(c, d);
    }
}
