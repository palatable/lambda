package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.monoid.Monoid;

public interface MonoidFactory<A, B> extends SemigroupFactory<A, B> {

    @Override
    Monoid<B> checkedApply(A a) throws Throwable;

    @Override
    default B apply(A a, B b, B c) {
        return apply(a).apply(b, c);
    }

    @Override
    default Monoid<B> apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }
}
