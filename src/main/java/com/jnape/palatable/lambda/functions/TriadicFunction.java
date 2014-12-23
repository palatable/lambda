package com.jnape.palatable.lambda.functions;

@FunctionalInterface
public interface TriadicFunction<A, B, C, D> extends DyadicFunction<A, B, MonadicFunction<C, D>> {

    public D apply(A a, B b, C c);

    @Override
    default DyadicFunction<B, C, D> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    @Override
    default MonadicFunction<C, D> apply(A a, B b) {
        return (c) -> apply(a, b, c);
    }

    @Override
    default TriadicFunction<B, A, C, D> flip() {
        return (b, a, c) -> apply(a, b, c);
    }
}
