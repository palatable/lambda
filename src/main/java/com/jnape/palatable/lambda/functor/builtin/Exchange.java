package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.optics.Iso;

/**
 * A profunctor used to extract the isomorphic functions an {@link Iso} is composed of.
 *
 * @param <A> the smaller viewed value of an {@link Iso}
 * @param <B> the smaller viewing value of an {@link Iso}
 * @param <S> the larger viewing value of an {@link Iso}
 * @param <T> the larger viewed value of an {@link Iso}
 */
public final class Exchange<A, B, S, T> implements Profunctor<S, T, Exchange<A, B, ?, ?>> {
    private final Fn1<? super S, ? extends A> sa;
    private final Fn1<? super B, ? extends T> bt;

    public Exchange(Fn1<? super S, ? extends A> sa, Fn1<? super B, ? extends T> bt) {
        this.sa = sa;
        this.bt = bt;
    }

    /**
     * Extract the mapping <code>S -&gt; A</code>.
     *
     * @return an <code>{@link Fn1}&lt;S, A&gt;</code>
     */
    public Fn1<? super S, ? extends A> sa() {
        return sa;
    }

    /**
     * Extract the mapping <code>B -&gt; T</code>.
     *
     * @return an <code>{@link Fn1}&lt;B, T&gt;</code>
     */
    public Fn1<? super B, ? extends T> bt() {
        return bt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Z, C> Exchange<A, B, Z, C> diMap(Fn1<? super Z, ? extends S> lFn,
                                             Fn1<? super T, ? extends C> rFn) {
        return new Exchange<>(lFn.fmap(sa), bt.fmap(rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Z> Exchange<A, B, Z, T> diMapL(Fn1<? super Z, ? extends S> fn) {
        return (Exchange<A, B, Z, T>) Profunctor.super.<Z>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Exchange<A, B, S, C> diMapR(Fn1<? super T, ? extends C> fn) {
        return (Exchange<A, B, S, C>) Profunctor.super.<C>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <Z> Exchange<A, B, Z, T> contraMap(Fn1<? super Z, ? extends S> fn) {
        return (Exchange<A, B, Z, T>) Profunctor.super.<Z>contraMap(fn);
    }
}
