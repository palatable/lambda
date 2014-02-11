package com.jnape.palatable.lambda;

import com.jnape.palatable.lambda.tuples.Tuple2;

public abstract class DyadicFunction<A, B, C> extends MonadicFunction<Tuple2<A, B>, C> {
    @Override
    public final C apply(Tuple2<A, B> args) {
        return apply(args._1, args._2);
    }

    public abstract C apply(A a, B b);

    public final DyadicFunction<B, A, C> flip() {
        return new DyadicFunction<B, A, C>() {
            @Override
            public C apply(B b, A a) {
                return DyadicFunction.this.apply(a, b);
            }
        };
    }

    public final MonadicFunction<B, C> partial(final A a) {
        return new MonadicFunction<B, C>() {
            @Override
            public C apply(B b) {
                return DyadicFunction.this.apply(a, b);
            }
        };
    }
}
