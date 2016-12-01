package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.semigroup.Semigroup;

@FunctionalInterface
public interface SemigroupFactory<A, B> extends Fn3<A, B, B, B> {

    @Override
    Semigroup<B> apply(A a);

    @Override
    default B apply(A a, B b, B c) {
        return apply(a).apply(b, c);
    }
}
