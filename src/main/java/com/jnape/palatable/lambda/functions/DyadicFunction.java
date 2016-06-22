package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Profunctor;

/**
 * A function taking two arguments. Auto-curried.
 *
 * @param <A> The first argument type
 * @param <B> The second argument type
 * @param <C> The return type
 * @see MonadicFunction
 * @see com.jnape.palatable.lambda.functions.builtin.dyadic.Partial2
 */
@FunctionalInterface
public interface DyadicFunction<A, B, C> extends MonadicFunction<A, MonadicFunction<B, C>>, Profunctor<B, C> {

    C apply(A a, B b);

    @Override
    default MonadicFunction<B, C> apply(A a) {
        return (b) -> apply(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <C1> DyadicFunction<A, C1, C> diMapL(MonadicFunction<? super C1, ? extends B> fn) {
        return (DyadicFunction<A, C1, C>) Profunctor.super.diMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <C1> DyadicFunction<A, B, C1> diMapR(MonadicFunction<? super C, ? extends C1> fn) {
        return (DyadicFunction<A, B, C1>) Profunctor.super.diMapR(fn);
    }

    @Override
    default <D, E> DyadicFunction<A, D, E> diMap(MonadicFunction<? super D, ? extends B> f1,
                                                 MonadicFunction<? super C, ? extends E> f2) {
        return (a, d) -> f2.apply(apply(a, f1.apply(d)));
    }

    default DyadicFunction<B, A, C> flip() {
        return (b, a) -> apply(a, b);
    }

    default MonadicFunction<Tuple2<A, B>, C> uncurry() {
        return (ab) -> apply(ab._1(), ab._2());
    }
}
