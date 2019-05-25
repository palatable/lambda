package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Identity;

import static com.jnape.palatable.lambda.optics.Optic.reframe;

/**
 * A generic supertype representation for a profunctor {@link Optic} that requires a {@link Pure} implementation to
 * derive its {@link Functor} constraint and graduate to a full-fledge {@link Optic}, equipped with a default
 * {@link Optic} instance for the profunctor constraint and {@link Identity}.
 *
 * @param <P> the {@link Profunctor} bound
 * @param <S> the left side of the output profunctor
 * @param <T> the right side's functor embedding of the output profunctor
 * @param <A> the left side of the input profunctor
 * @param <B> the right side's functor embedding of the input profunctor
 */
@FunctionalInterface
public interface ProtoOptic<P extends Profunctor<?, ?, ? extends P>, S, T, A, B>
        extends Optic<P, Identity<?>, S, T, A, B> {

    /**
     * Given a {@link Pure} lifting function, fix this {@link ProtoOptic} to the given {@link Functor} and promote it to
     * an {@link Optic}.
     *
     * @param pure the {@link Pure} lifting function
     * @param <F>  the {@link Functor} bound
     * @return the {@link Optic}
     */
    <F extends Applicative<?, F>> Optic<P, F, S, T, A, B> toOptic(Pure<F> pure);

    /**
     * {@inheritDoc}
     */
    @Override
    default <CoP extends Profunctor<?, ?, ? extends P>, CoF extends Functor<?, ? extends Identity<?>>,
            FB extends Functor<B, ? extends CoF>, FT extends Functor<T, ? extends CoF>,
            PAFB extends Profunctor<A, FB, ? extends CoP>,
            PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
        return toOptic(Pure.<Identity<?>>pure(Identity::new)).apply(pafb);
    }

    /**
     * Left-to-right composition of proto-optics. Requires compatibility between <code>S</code> and <code>T</code>.
     *
     * @param f   the other proto-optic
     * @param <Z> the new left side of the input profunctor
     * @param <C> the new right side's functor embedding of the input profunctor
     * @return the composed proto-optic
     */
    default <Z, C> ProtoOptic<P, S, T, Z, C> andThen(ProtoOptic<? super P, A, B, Z, C> f) {
        return new ProtoOptic<P, S, T, Z, C>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, S, T, Z, C> toOptic(Pure<F> pure) {
                Optic<? super P, F, A, B, Z, C> optic = f.toOptic(pure);
                return ProtoOptic.this.toOptic(pure).andThen(reframe(optic));
            }
        };
    }

    /**
     * Right-to-Left composition of proto-optics. Requires compatibility between <code>A</code> and <code>B</code>.
     *
     * @param g   the other proto-optic
     * @param <R> the new left side of the output profunctor
     * @param <U> the new right side's functor embedding of the output profunctor
     * @return the composed proto-optic
     */
    default <R, U> ProtoOptic<P, R, U, A, B> compose(ProtoOptic<? super P, R, U, S, T> g) {
        return new ProtoOptic<P, R, U, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, R, U, A, B> toOptic(Pure<F> pure) {
                Optic<? super P, F, R, U, S, T> optic = g.toOptic(pure);
                return ProtoOptic.this.toOptic(pure).compose(reframe(optic));
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <R> ProtoOptic<P, R, T, A, B> mapS(Fn1<? super R, ? extends S> fn) {
        return new ProtoOptic<P, R, T, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, R, T, A, B> toOptic(Pure<F> pure) {
                return ProtoOptic.this.toOptic(pure).mapS(fn);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> ProtoOptic<P, S, U, A, B> mapT(Fn1<? super T, ? extends U> fn) {
        return new ProtoOptic<P, S, U, A, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, S, U, A, B> toOptic(Pure<F> pure) {
                return ProtoOptic.this.toOptic(pure).mapT(fn);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <C> ProtoOptic<P, S, T, C, B> mapA(Fn1<? super A, ? extends C> fn) {
        return new ProtoOptic<P, S, T, C, B>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, S, T, C, B> toOptic(Pure<F> pure) {
                return ProtoOptic.this.toOptic(pure).mapA(fn);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <Z> ProtoOptic<P, S, T, A, Z> mapB(Fn1<? super Z, ? extends B> fn) {
        return new ProtoOptic<P, S, T, A, Z>() {
            @Override
            public <F extends Applicative<?, F>> Optic<P, F, S, T, A, Z> toOptic(Pure<F> pure) {
                return ProtoOptic.this.toOptic(pure).mapB(fn);
            }
        };
    }
}
