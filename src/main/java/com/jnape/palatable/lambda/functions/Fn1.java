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
public interface Fn1<A, B> extends Functor<B>, Profunctor<A, B>, Function<A, B> {

    B apply(A a);

    default <C> Fn1<A, C> then(Fn1<? super B, ? extends C> g) {
        return fmap(g);
    }

    @Override
    default <C> Fn1<A, C> fmap(Fn1<? super B, ? extends C> g) {
        return a -> g.apply(apply(a));
    }

    @Override
    default <Z> Fn1<Z, B> diMapL(Fn1<Z, A> fn) {
        return (Fn1<Z, B>) Profunctor.super.diMapL(fn);
    }

    @Override
    default <C> Fn1<A, C> diMapR(Fn1<B, C> fn) {
        return (Fn1<A, C>) Profunctor.super.diMapR(fn);
    }

    @Override
    default <C, D> Fn1<C, D> diMap(Fn1<C, A> lFn, Fn1<B, D> rFn) {
        return lFn.andThen(this).andThen(rFn);
    }

    @Override
    default <Z> Fn1<Z, B> compose(Function<? super Z, ? extends A> before) {
        return z -> apply(before.apply(z));
    }

    @Override
    default <C> Fn1<A, C> andThen(Function<? super B, ? extends C> after) {
        return a -> after.apply(apply(a));
    }
}
