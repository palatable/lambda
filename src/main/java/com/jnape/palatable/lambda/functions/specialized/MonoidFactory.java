package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.monoid.Monoid;

public interface MonoidFactory<A, B> extends SemigroupFactory<A, B> {

    @Override
    default B apply(A a, B b, B c) {
        return apply(a).apply(b, c);
    }

    @Override
    Monoid<B> apply(A a);
}
