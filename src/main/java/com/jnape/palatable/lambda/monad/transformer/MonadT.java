package com.jnape.palatable.lambda.monad.transformer;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadBase;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.builtin.EitherT;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;
import com.jnape.palatable.lambda.monad.transformer.builtin.ReaderT;

/**
 * The generic type representing a {@link Monad} transformer, exposing the argument {@link Monad} as a type parameter.
 * <p>
 * While any two {@link Functor functors} and any two {@link Applicative applicatives} can be composed in general, the
 * same is not true in general of any two {@link Monad monads}. However, there exist {@link Monad monads} that do
 * compose, in general, with any other {@link Monad}. When this is the case, the combination of these
 * {@link Monad monads} with any other {@link Monad} can offer composed implementations of {@link Monad#pure pure} and
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
 * @param <M>  the argument {@link Monad monad}
 * @param <A>  the carrier type
 * @param <MT> the {@link Monad} witness
 * @param <T>  the {@link MonadT} witness
 * @see Monad
 * @see MonadBase
 * @see MaybeT
 * @see EitherT
 * @see ReaderT
 */
public interface MonadT<M extends MonadRec<?, M>, A, MT extends MonadT<M, ?, MT, T>, T extends MonadT<?, ?, ?, T>>
        extends MonadBase<M, A, T>, Monad<A, MT>, MonadRec<A, MT> {

    /**
     * {@inheritDoc}
     */
    @Override
    <B, N extends MonadRec<?, N>> MonadT<N, B, ?, T> lift(MonadRec<B, N> mb);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadT<M, B, MT, T> flatMap(Fn1<? super A, ? extends Monad<B, MT>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadT<M, B, MT, T> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<M, B, MT, T> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadRec.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<M, B, MT, T> zip(Applicative<Fn1<? super A, ? extends B>, MT> appFn) {
        return MonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Lazy<? extends MonadT<M, B, MT, T>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, MT>> lazyAppFn) {
        return MonadRec.super.lazyZip(lazyAppFn).fmap(Applicative<B, MT>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<M, B, MT, T> discardL(Applicative<B, MT> appB) {
        return MonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<M, A, MT, T> discardR(Applicative<B, MT> appB) {
        return MonadRec.super.discardR(appB).coerce();
    }
}
