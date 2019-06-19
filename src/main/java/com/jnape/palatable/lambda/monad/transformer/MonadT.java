package com.jnape.palatable.lambda.monad.transformer;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.builtin.EitherT;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;
import com.jnape.palatable.lambda.monad.transformer.builtin.ReaderT;

/**
 * While any two {@link Functor functors} and any two {@link Applicative applicatives} can be composed in general, the
 * same is not true in general of any two {@link Monad monads}, in general. However, there exist {@link Monad monads}
 * that do compose, in general, with any other {@link Monad}. When this is the case, the combination of these
 * {@link Monad monads} with any other {@link Monad} can offer implementations of {@link Monad#pure pure} and
 * {@link Monad#flatMap(Fn1) flatMap} for free, simply by relying on the other {@link Monad monad's} implementation of
 * both, as well as their own privileged knowledge about how to merge the nested {@link Monad#flatMap(Fn1) flatMap}
 * call. This can be thought of as "gluing" together two {@link Monad monads}, allowing easier access to their values,
 * as well as, in some cases, providing universally correct constructions of the composed short-circuiting algorithms.
 * <p>
 * The term "monad transformer" describes this particular encoding of monadic composition, and tends to be
 * named in terms of {@link Monad} for which privileged knowledge must be known in order to eliminate during
 * {@link Monad#flatMap(Fn1) flatmapping}.
 * <p>
 * For more information, <a href="https://en.wikipedia.org/wiki/Monad_transformer" target="_blank">read more about</a>
 * monad transformers.
 *
 * @param <F> the outer {@link Monad monad}
 * @param <G> the inner {@link Monad monad}
 * @param <A> the carrier type
 * @see MaybeT
 * @see EitherT
 * @see ReaderT
 */
public interface MonadT<F extends Monad<?, F>, G extends Monad<?, G>, A, MT extends MonadT<F, G, ?, MT>>
        extends Monad<A, MT> {

    /**
     * Extract out the composed monad out of this transformer.
     *
     * @param <GA>  the inferred embedded monad
     * @param <FGA> the inferred composed monad
     * @return the composed monad
     */
    <GA extends Monad<A, G>, FGA extends Monad<GA, F>> FGA run();

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadT<F, G, B, MT> flatMap(Fn1<? super A, ? extends Monad<B, MT>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadT<F, G, B, MT> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B, MT> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B, MT> zip(Applicative<Fn1<? super A, ? extends B>, MT> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Lazy<? extends MonadT<F, G, B, MT>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, MT>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<B, MT>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B, MT> discardL(Applicative<B, MT> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, A, MT> discardR(Applicative<B, MT> appB) {
        return Monad.super.discardR(appB).coerce();
    }
}
