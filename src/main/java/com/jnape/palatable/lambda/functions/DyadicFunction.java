package com.jnape.palatable.lambda.functions;

@FunctionalInterface
public interface DyadicFunction<A, B, C> extends MonadicFunction<A, MonadicFunction<B, C>> {

    public C apply(A a, B b);

    @Override
    default MonadicFunction<B, C> apply(A a) {
        return (b) -> DyadicFunction.this.apply(a, b);
    }

    public default DyadicFunction<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }
}
