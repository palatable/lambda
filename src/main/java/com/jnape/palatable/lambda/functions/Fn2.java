package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;

/**
 * A function taking two arguments. Auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The return type
 * @see Fn1
 * @see com.jnape.palatable.lambda.functions.builtin.fn2.Partial2
 */
@FunctionalInterface
public interface Fn2<A, B, C> extends Fn1<A, Fn1<B, C>> {

    C apply(A a, B b);

    @Override
    default Fn1<B, C> apply(A a) {
        return (b) -> apply(a, b);
    }

    default Fn2<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    default Fn1<Tuple2<A, B>, C> uncurry() {
        return (ab) -> apply(ab._1(), ab._2());
    }
}
