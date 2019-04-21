package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;

/**
 * A generic supertype representation for profunctor optics.
 * <p>
 * Precisely stated, for some {@link Profunctor} <code>P</code> and some {@link Functor} <code>F</code>, and for the
 * types <code>S</code> <code>T</code> <code>A</code> <code>B</code>, an
 * <code>{@link Optic}&lt;P, F, S, T, A, B&gt;</code> is a polymorphic function
 * <code>(P&lt;A, F&lt;B&gt;&gt; -&gt; P&lt;S, F&lt;T&gt;&gt;)</code> (existentially-quantified allowing for
 * covariance).
 *
 * @param <P> the {@link Profunctor} type
 * @param <F> the {@link Functor} type
 * @param <S> the left side of the output profunctor
 * @param <T> the right side's functor embedding of the output profunctor
 * @param <A> the left side of the input profunctor
 * @param <B> the right side's functor embedding of the input profunctor
 */
@FunctionalInterface
public interface Optic<P extends Profunctor<?, ?, P>, F extends Functor<?, F>, S, T, A, B> {

    <CoP extends P, CoF extends F,
            PAFB extends Profunctor<A, ? extends Functor<B, ? extends CoF>, ? extends CoP>,
            PSFT extends Profunctor<S, ? extends Functor<T, ? extends CoF>, ? extends CoP>> PSFT apply(PAFB pafb);

    /**
     * Produce a monomorphic {@link Fn1} backed by this {@link Optic}.
     *
     * @param <CoP>  the covariant bound on P
     * @param <CoF>  the covariant bound on F
     * @param <PAFB> the fixed input profunctor type
     * @param <PSFT> the fixed output profunctor type
     * @return the monomorphic {@link Fn1} backed by this {@link Optic}
     */
    default <CoP extends P, CoF extends F,
            PAFB extends Profunctor<A, ? extends Functor<B, ? extends CoF>, ? extends CoP>,
            PSFT extends Profunctor<S, ? extends Functor<T, ? extends CoF>, ? extends CoP>>
    Fn1<PAFB, PSFT> monomorphize() {
        return Optic.this::<CoP, CoF, PAFB, PSFT>apply;
    }
}
