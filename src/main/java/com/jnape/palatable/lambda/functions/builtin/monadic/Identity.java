package com.jnape.palatable.lambda.functions.builtin.monadic;

import com.jnape.palatable.lambda.functions.MonadicFunction;

public final class Identity<A> extends MonadicFunction<A, A> {

    @Override
    public final A apply(A a) {
        return a;
    }

    public static <A> Identity<A> id() {
        return new Identity<A>();
    }
}
