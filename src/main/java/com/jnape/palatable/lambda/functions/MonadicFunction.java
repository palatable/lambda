package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;

import java.util.function.Function;

/**
 * A function taking a single argument. This is the core function type that all other function types extend and
 * auto-curry with.
 *
 * @param <A> The input type
 * @param <B> The output type
 */
@FunctionalInterface
public interface MonadicFunction<A, B> extends Functor<B>, Profunctor<A, B>, Function<A, B> {

    B apply(A a);

    default <C> MonadicFunction<A, C> then(MonadicFunction<? super B, ? extends C> g) {
        return fmap(g);
    }

    @Override
    default <C> MonadicFunction<A, C> fmap(MonadicFunction<? super B, ? extends C> g) {
        return a -> g.apply(apply(a));
    }

    @Override
    default <Z> MonadicFunction<Z, B> diMapL(MonadicFunction<Z, A> fn) {
        return (MonadicFunction<Z, B>) Profunctor.super.diMapL(fn);
    }

    @Override
    default <C> MonadicFunction<A, C> diMapR(MonadicFunction<B, C> fn) {
        return (MonadicFunction<A, C>) Profunctor.super.diMapR(fn);
    }

    @Override
    default <C, D> MonadicFunction<C, D> diMap(MonadicFunction<C, A> lFn, MonadicFunction<B, D> rFn) {
        return lFn.andThen(this).andThen(rFn);
    }

    @Override
    default <Z> MonadicFunction<Z, B> compose(Function<? super Z, ? extends A> before) {
        return z -> apply(before.apply(z));
    }

    @Override
    default <C> MonadicFunction<A, C> andThen(Function<? super B, ? extends C> after) {
        return a -> after.apply(apply(a));
    }
}
