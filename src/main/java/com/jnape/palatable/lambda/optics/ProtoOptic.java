package com.jnape.palatable.lambda.optics;

import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.Profunctor;
import com.jnape.palatable.lambda.functor.builtin.Identity;

/**
 * A generic supertype representation for a profunctor {@link Optic} that requires a {@link Pure} implementation to
 * derive its {@link Functor} constraint and graduate to a full-fledge {@link Optic}.
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

    @Override
    default <CoP extends Profunctor<?, ?, ? extends P>, CoF extends Functor<?, ? extends Identity<?>>,
            FB extends Functor<B, ? extends CoF>, FT extends Functor<T, ? extends CoF>,
            PAFB extends Profunctor<A, FB, ? extends CoP>,
            PSFT extends Profunctor<S, FT, ? extends CoP>> PSFT apply(PAFB pafb) {
        return toOptic(Pure.<Identity<?>>pure(Identity::new)).apply(pafb);
    }
}
