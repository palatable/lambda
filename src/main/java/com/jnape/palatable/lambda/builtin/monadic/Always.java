package com.jnape.palatable.lambda.builtin.monadic;

import com.jnape.palatable.lambda.MonadicFunction;

public class Always {

    public static <A, B> MonadicFunction<A, B> always(final B b) {
        return new MonadicFunction<A, B>() {
            @Override
            public B apply(A a) {
                return b;
            }
        };
    }
}
