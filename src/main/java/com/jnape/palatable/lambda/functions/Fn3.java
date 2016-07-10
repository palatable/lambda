package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

/**
 * A function taking three arguments. Auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The third argument type
 * @param <D> The return type
 * @see Fn2
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.Partial3
 */
@FunctionalInterface
public interface Fn3<A, B, C, D> extends Fn2<A, B, Fn1<C, D>> {

    D apply(A a, B b, C c);

    @Override
    default Fn2<B, C, D> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    @Override
    default Fn1<C, D> apply(A a, B b) {
        return (c) -> apply(a, b, c);
    }

    @Override
    default Fn3<B, A, C, D> flip() {
        return (b, a, c) -> apply(a, b, c);
    }

    @Override
    default Fn2<Tuple2<A, B>, C, D> uncurry() {
        return (ab, c) -> apply(ab._1(), ab._2(), c);
    }
}
