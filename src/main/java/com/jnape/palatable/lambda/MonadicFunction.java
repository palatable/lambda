package com.jnape.palatable.lambda;

public abstract class MonadicFunction<A, B> {

    public abstract B apply(A a);

    public final <C> MonadicFunction<A, C> then(final MonadicFunction<B, C> f) {
        return new MonadicFunction<A, C>() {
            @Override
            public C apply(A a) {
                MonadicFunction<A, B> g = MonadicFunction.this;
                return f.apply(g.apply(a));
            }
        };
    }
}
