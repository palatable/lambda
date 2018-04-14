package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.lens.Iso;

import java.util.function.Function;

/**
 * A profunctor used to extract the isomorphic functions an {@link Iso} is composed of.
 *
 * @param <A> the smaller viewed value of an {@link Iso}
 * @param <B> the smaller viewing value of an {@link Iso}
 * @param <S> the larger viewing value of an {@link Iso}
 * @param <T> the larger viewed value of an {@link Iso}
 */
public final class Exchange<A, B, S, T> implements Profunctor<S, T, Exchange<A, B, ?, ?>> {
    private final Function<? super S, ? extends A> sa;
    private final Function<? super B, ? extends T> bt;

    public Exchange(Function<? super S, ? extends A> sa, Function<? super B, ? extends T> bt) {
        this.sa = sa;
        this.bt = bt;
    }

    public Function<? super S, ? extends A> sa() {
        return sa;
    }

    public Function<? super B, ? extends T> bt() {
        return bt;
    }

    @Override
    public <Z, C> Exchange<A, B, Z, C> diMap(Function<? super Z, ? extends S> lFn,
                                             Function<? super T, ? extends C> rFn) {
        return new Exchange<>(lFn.andThen(sa), bt.andThen(rFn));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Z> Exchange<A, B, Z, T> diMapL(Function<? super Z, ? extends S> fn) {
        return (Exchange<A, B, Z, T>) Profunctor.super.diMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> Exchange<A, B, S, C> diMapR(Function<? super T, ? extends C> fn) {
        return (Exchange<A, B, S, C>) Profunctor.super.diMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Z> Exchange<A, B, Z, T> contraMap(Function<? super Z, ? extends S> fn) {
        return (Exchange<A, B, Z, T>) Profunctor.super.contraMap(fn);
    }
}
