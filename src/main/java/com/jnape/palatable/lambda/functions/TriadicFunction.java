package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.tuples.Tuple3;

import static com.jnape.palatable.lambda.functions.builtin.dyadic.Partial3.partial3;

public abstract class TriadicFunction<A, B, C, D> extends MonadicFunction<Tuple3<A, B, C>, D> {

    @Override
    public final D apply(Tuple3<A, B, C> args) {
        return apply(args._1, args._2, args._3);
    }

    public abstract D apply(A a, B b, C c);

    public final DyadicFunction<B, C, D> partial(final A a) {
        return partial3(this, a);
    }
}
