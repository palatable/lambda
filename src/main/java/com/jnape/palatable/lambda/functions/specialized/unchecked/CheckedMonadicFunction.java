package com.jnape.palatable.lambda.functions.specialized.unchecked;

import com.jnape.palatable.lambda.functions.MonadicFunction;

import static com.jnape.palatable.lambda.functions.specialized.Checked.throwChecked;

public interface CheckedMonadicFunction<A, B> extends MonadicFunction<A, B> {

    @Override
    default B apply(A a) {
        try {
            return checkedApply(a);
        } catch (Throwable t) {
            throw throwChecked(t);
        }
    }

    B checkedApply(A a) throws Throwable;
}
