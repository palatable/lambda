package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.comonad.builtin.ComonadTraced;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.monoid.Monoid;

/**
 * A concrete implementation of the {@link ComonadTraced} interface.
 *
 * @param <A> the type of the input to the {@link Traced#trace} function
 * @param <M> the {@link Monoid} instance for A
 * @param <B> the type of the output to the {@link Traced#trace} function
 */
public final class Traced<A, M extends Monoid<A>, B> implements Comonad<B, Traced<A, M, ?>>, ComonadTraced<A, M, B, Traced<A, M, ?>> {
    private final Fn1<? super A, ? extends B> trace;
    private final Monoid<A> aMonoid;

    private Traced(Fn1<? super A, ? extends B> t, Monoid<A> m) {
        this.trace = t;
        this.aMonoid = m;
    }

    /**
     * Constructor function for Traced.
     *
     * @param t    the trace function
     * @param m    the {@link Monoid} instance for the input to t
     * @param <A>  the input type of t
     * @param <M>  the {@link Monoid} type of m
     * @param <B>  the output type of t
     * @return a   new instance of Traced&lt;A, M, B&gt;
     */
    public static <A, M extends Monoid<A>, B> Traced<A, M, B> traced(Fn1<? super A, ? extends B> t, Monoid<A> m) {
        return new Traced<>(t, m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final B runTrace(A a) {
        return trace.apply(a);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Monoid<A> getMonoid() {
        return aMonoid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Comonad<C, Traced<A, M, ?>> fmap(Fn1<? super B, ? extends C> fn) {
        return ComonadTraced.super.<C>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public B extract() {
        return trace.apply(aMonoid.identity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Comonad<C, Traced<A, M, ?>> extendImpl(Fn1<? super Comonad<B, Traced<A, M, ?>>, ? extends C> f) {
        return traced(a -> f.apply(traced(trace.diMapL(aMonoid.apply(a)), aMonoid)), aMonoid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C, WA extends Comonad<B, Traced<A, M, ?>>> Traced<A, M, C> extend(Fn1<? super WA, ? extends C> f) {
        return ComonadTraced.super.<C, WA>extend(f).coerce();
    }
}
