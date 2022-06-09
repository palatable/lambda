package com.jnape.palatable.lambda.functions.specialized;

import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.internal.Runtime;
import com.jnape.palatable.lambda.semigroup.Semigroup;

@FunctionalInterface
public interface SemigroupFactory<A, B> extends Fn3<A, B, B, B> {

    @Override
    Semigroup<B> checkedApply(A a) throws Throwable;

    @Override
    default Semigroup<B> apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw Runtime.throwChecked(t);
        }
    }

    @Override
    default B checkedApply(A a, B b, B c) throws Throwable {
        return checkedApply(a).checkedApply(b, c);
    }
}
