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
 * @param <P> the {@link Profunctor} bound
 * @param <F> the {@link Functor} bound
 * @param <S> the left side of the output profunctor
 * @param <T> the right side's functor embedding of the output profunctor
 * @param <A> the left side of the input profunctor
 * @param <B> the right side's functor embedding of the input profunctor
 */
@FunctionalInterface
public interface Optic<P extends Profunctor<?, ?, ? extends P>, F extends Functor<?, ? extends F>, S, T, A, B> {

    <CoP extends Profunctor<?, ?, ? extends P>,
            CoF extends Functor<?, ? extends F>,
            FB extends Functor<B, ? extends CoF>,
            FT extends Functor<T, ? extends CoF>,
            PAFB extends Profunctor<A, FB, ? extends CoP>,
            PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb);

    /**
     * Produce a monomorphic {@link Fn1} backed by this {@link Optic}.
     *
     * @param <CoP>  the covariant bound on P
     * @param <CoF>  the covariant bound on F
     * @param <FB>   fixed functor over B for inference
     * @param <FT>   fixed functor over T for inference
     * @param <PAFB> the fixed input profunctor type
     * @param <PSFT> the fixed output profunctor type
     * @return the monomorphic {@link Fn1} backed by this {@link Optic}
     */
    default <CoP extends Profunctor<?, ?, ? extends P>,
            CoF extends Functor<?, ? extends F>,
            FB extends Functor<B, ? extends CoF>,
            FT extends Functor<T, ? extends CoF>,
            PAFB extends Profunctor<A, FB, ? extends CoP>,
            PSFT extends Profunctor<S, FT, ? extends CoP>>
    Fn1<PAFB, PSFT> monomorphize() {
        return this::apply;
    }

    /**
     * Left-to-right composition of optics. Requires compatibility between <code>S</code> and <code>T</code>.
     *
     * @param f   the other optic
     * @param <Z> the new left side of the input profunctor
     * @param <C> the new right side's functor embedding of the input profunctor
     * @return the composed optic
     */
    default <Z, C> Optic<P, F, S, T, Z, C> andThen(Optic<? super P, ? super F, A, B, Z, C> f) {
        return new Optic<P, F, S, T, Z, C>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends P>,
                    CoF extends Functor<?, ? extends F>,
                    FC extends Functor<C, ? extends CoF>,
                    FT extends Functor<T, ? extends CoF>,
                    PZFC extends Profunctor<Z, FC, ? extends CoP>,
                    PSFT extends Profunctor<S, FT, ? extends CoP>>
            PSFT apply(PZFC pzfc) {
                return Optic.this.apply(f.apply(pzfc));
            }
        };
    }

    /**
     * Right-to-Left composition of optics. Requires compatibility between <code>A</code> and <code>B</code>.
     *
     * @param g   the other optic
     * @param <R> the new left side of the output profunctor
     * @param <U> the new right side's functor embedding of the output profunctor
     * @return the composed optic
     */
    default <R, U> Optic<P, F, R, U, A, B> compose(Optic<? super P, ? super F, R, U, S, T> g) {
        return new Optic<P, F, R, U, A, B>() {
            @Override
            public <CoP extends Profunctor<?, ?, ? extends P>,
                    CoF extends Functor<?, ? extends F>,
                    FB extends Functor<B, ? extends CoF>,
                    FU extends Functor<U, ? extends CoF>,
                    PAFB extends Profunctor<A, FB, ? extends CoP>,
                    PRFU extends Profunctor<R, FU, ? extends CoP>>
            PRFU apply(PAFB pafb) {
                return g.<CoP, CoF, Functor<T, ? extends CoF>, FU,
                        Profunctor<S, Functor<T, ? extends CoF>, ? extends CoP>, PRFU>apply(Optic.this.apply(pafb));
            }
        };
    }

    /**
     * Contravariantly map <code>S</code> to <code>R</code>, yielding a new optic.
     *
     * @param fn  the mapping function
     * @param <R> the new left side of the output profunctor
     * @return the new optic
     */
    default <R> Optic<P, F, R, T, A, B> mapS(Fn1<? super R, ? extends S> fn) {
        return optic(pafb -> {
            Profunctor<S, Functor<T, ? extends F>, ? extends P> psft = apply(pafb);
            return psft.diMapL(fn);
        });
    }

    /**
     * Covariantly map <code>T</code> to <code>U</code>, yielding a new optic.
     *
     * @param fn  the mapping function
     * @param <U> the new right side's functor embedding of the output profunctor
     * @return the new optic
     */
    default <U> Optic<P, F, S, U, A, B> mapT(Fn1<? super T, ? extends U> fn) {
        return optic(pafb -> {
            Profunctor<S, Functor<T, ? extends F>, ? extends P> psft = apply(pafb);
            return psft.diMapR(ft -> ft.fmap(fn));
        });
    }

    /**
     * Covariantly map <code>A</code> to <code>C</code>, yielding a new optic.
     *
     * @param fn  the mapping function
     * @param <C> the new left side of the input profunctor
     * @return the new optic
     */
    default <C> Optic<P, F, S, T, C, B> mapA(Fn1<? super A, ? extends C> fn) {
        return optic(pcfb -> {
            @SuppressWarnings("UnnecessaryLocalVariable")
            Profunctor<S, Functor<T, ? extends F>, ? extends P> psft = apply(pcfb.diMapL(fn));
            return psft;
        });
    }

    /**
     * Contravariantly map <code>B</code> to <code>Z</code>, yielding a new optic.
     *
     * @param <Z> the new right side's functor embedding of the input profunctor
     * @param fn  the mapping function
     * @return the new optic
     */
    default <Z> Optic<P, F, S, T, A, Z> mapB(Fn1<? super Z, ? extends B> fn) {
        return optic(pafz -> apply(pafz.diMapR(fz -> fz.fmap(fn))));
    }

    /**
     * Promote a monomorphic function to a compatible {@link Optic}.
     *
     * @param fn     the function
     * @param <P>    the {@link Profunctor} bound
     * @param <F>    the {@link Functor} bound
     * @param <S>    the left side of the output profunctor
     * @param <T>    the right side's functor embedding of the output profunctor
     * @param <A>    the left side of the input profunctor
     * @param <B>    the right side's functor embedding of the input profunctor
     * @param <FB>   fixed functor over B for inference
     * @param <FT>   fixed functor over T for inference
     * @param <PAFB> the input
     * @param <PSFT> the output
     * @return the {@link Optic}
     */
    static <P extends Profunctor<?, ?, ? extends P>,
            F extends Functor<?, ? extends F>,
            S, T, A, B,
            FB extends Functor<B, ? extends F>,
            FT extends Functor<T, ? extends F>,
            PAFB extends Profunctor<A, FB, ? extends P>,
            PSFT extends Profunctor<S, FT, ? extends P>> Optic<P, F, S, T, A, B> optic(Fn1<PAFB, PSFT> fn) {
        return new Optic<P, F, S, T, A, B>() {
            @Override
            @SuppressWarnings("unchecked")
            public <CoP extends Profunctor<?, ?, ? extends P>,
                    CoF extends Functor<?, ? extends F>,
                    CoFB extends Functor<B, ? extends CoF>,
                    CoFT extends Functor<T, ? extends CoF>,
                    CoPAFB extends Profunctor<A, CoFB, ? extends CoP>,
                    CoPSFT extends Profunctor<S, CoFT, ? extends CoP>> CoPSFT apply(
                    CoPAFB pafb) {
                return (CoPSFT) fn.apply((PAFB) pafb);
            }
        };
    }

    /**
     * Reframe an {@link Optic} according to covariant bounds.
     *
     * @param optic the {@link Optic}
     * @param <P>   the {@link Profunctor} type
     * @param <F>   the {@link Functor} type
     * @param <S>   the left side of the output profunctor
     * @param <T>   the right side's functor embedding of the output profunctor
     * @param <A>   the left side of the input profunctor
     * @param <B>   the right side's functor embedding of the input profunctor
     * @return the covariantly reframed {@link Optic}
     */
    static <P extends Profunctor<?, ?, ? extends P>,
            F extends Functor<?, ? extends F>,
            S, T, A, B> Optic<P, F, S, T, A, B> reframe(Optic<? super P, ? super F, S, T, A, B> optic) {
        return Optic.optic(optic.<P, F,
                Functor<B, ? extends F>,
                Functor<T, ? extends F>,
                Profunctor<A, Functor<B, ? extends F>, ? extends P>,
                Profunctor<S, Functor<T, ? extends F>, ? extends P>>monomorphize());
    }

    /**
     * An convenience type with a simplified signature for {@link Optic optics} with unified <code>S/T</code> and
     * <code>A/B</code> types.
     *
     * @param <P> the {@link Profunctor} bound
     * @param <F> the {@link Functor} bound
     * @param <S> the left side and right side's functor embedding of the output profunctor
     * @param <A> the left side and right side's functor embedding of the input profunctor
     */
    interface Simple<P extends Profunctor<?, ?, ? extends P>, F extends Functor<?, ? extends F>, S, A>
            extends Optic<P, F, S, S, A, A> {

        /**
         * Compose two simple optics from left to right.
         *
         * @param f   the other simple optic
         * @param <B> the new left side and right side's functor embedding of the input profunctor
         * @return the composed simple optic
         */
        @SuppressWarnings("overloads")
        default <B> Optic.Simple<P, F, S, B> andThen(Optic.Simple<? super P, ? super F, A, B> f) {
            Optic<P, F, S, S, B, B> composed = Optic.super.andThen(f);
            return new Simple<P, F, S, B>() {
                @Override
                public <CoP extends Profunctor<?, ?, ? extends P>, CoF extends Functor<?, ? extends F>,
                        FB extends Functor<B, ? extends CoF>, FT extends Functor<S, ? extends CoF>,
                        PAFB extends Profunctor<B, FB, ? extends CoP>, PSFT extends Profunctor<S, FT, ? extends CoP>>
                PSFT apply(PAFB pafb) {
                    return composed.apply(pafb);
                }
            };
        }

        /**
         * Compose two simple optics from right to left.
         *
         * @param g   the other simple optic
         * @param <R> the new left side and right side's functor embedding of the output profunctor
         * @return the composed simple optic
         */
        @SuppressWarnings("overloads")
        default <R> Optic.Simple<P, F, R, A> compose(Optic.Simple<? super P, ? super F, R, S> g) {
            Optic<P, F, R, R, A, A> composed = Optic.super.compose(g);
            return new Simple<P, F, R, A>() {
                @Override
                public <CoP extends Profunctor<?, ?, ? extends P>, CoF extends Functor<?, ? extends F>,
                        FB extends Functor<A, ? extends CoF>, FT extends Functor<R, ? extends CoF>,
                        PAFB extends Profunctor<A, FB, ? extends CoP>, PSFT extends Profunctor<R, FT, ? extends CoP>>
                PSFT apply(PAFB pafb) {
                    return composed.apply(pafb);
                }
            };
        }

        /**
         * Adapt an {@link Optic} with S/T and A/B unified into a {@link Simple simple optic}.
         *
         * @param optic the {@link Optic}
         * @param <P>   the {@link Profunctor} bound
         * @param <F>   the {@link Functor} bound
         * @param <S>   the left side and the right side's functor embedding of the output profunctor
         * @param <A>   the left side and the right side's functor embedding of the input profunctor
         * @return the {@link Simple} optic
         */
        static <P extends Profunctor<?, ?, ? extends P>,
                F extends Functor<?, ? extends F>,
                S, A> Simple<P, F, S, A> adapt(Optic<? super P, ? super F, S, S, A, A> optic) {
            return new Simple<P, F, S, A>() {
                @Override
                public <CoP extends Profunctor<?, ?, ? extends P>, CoF extends Functor<?, ? extends F>,
                        FB extends Functor<A, ? extends CoF>, FT extends Functor<S, ? extends CoF>,
                        PAFB extends Profunctor<A, FB, ? extends CoP>,
                        PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
                    return optic.apply(pafb);
                }
            };
        }
    }
}
