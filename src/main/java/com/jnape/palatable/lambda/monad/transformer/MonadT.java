package com.jnape.palatable.lambda.monad.transformer;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.builtin.MaybeT;

/**
 * An interface representing a {@link Monad} transformer.
 * <p>
 * While any two {@link com.jnape.palatable.lambda.functor.Functor functors} and any two
 * {@link Applicative applicatives} can be composed in general, the same is not true in general of any two
 * {@link Monad monads}. However, there exist {@link Monad monads} that do compose, in general, with any other
 * {@link Monad}, provided that they are embedded inside the other {@link Monad}. When this is the case, they can offer
 * implementations of {@link Monad#pure pure} and {@link Monad#flatMap(Fn1) flatMap} for free, simply by relying
 * on the outer {@link Monad monad's} implementation of both, as well as their own privileged knowledge about how to
 * merge the nested {@link Monad#flatMap(Fn1) flatMap} call.
 * <p>
 * The term "monad transformer" describes a particular encoding of monadic composition. Because this general composition
 * of a particular {@link Monad} with any other {@link Monad} relies on privileged knowledge about the embedded
 * {@link Monad}, the {@link MonadT transformer} representing this compositions is described from the embedded
 * {@link Monad monad's} perspective (e.g. {@link MaybeT} describing the embedding
 * <code>{@link Monad}&lt;{@link com.jnape.palatable.lambda.adt.Maybe}&lt;A&gt;&gt;</code>).
 * <p>
 * Additionally, monad transformers connected by compatible {@link Monad monads} also compose. When two or more monad
 * transformers are composed, this is generally referred to as a "monad transformer stack".
 * <p>
 * For more information, <a href="https://en.wikipedia.org/wiki/Monad_transformer" target="_blank">read more about</a>
 * monad transformers.
 *
 * @param <F> the outer {@link Monad monad}
 * @param <G> the inner {@link Monad monad}
 * @param <A> the carrier type
 * @see MaybeT
 */
public interface MonadT<F extends Monad<?, F>, G extends Monad<?, G>, A>
        extends Monad<A, MonadT<F, G, ?>> {

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
    <B> MonadT<F, G, B> flatMap(Fn1<? super A, ? extends Monad<B, MonadT<F, G, ?>>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadT<F, G, B> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B> zip(Applicative<Fn1<? super A, ? extends B>, MonadT<F, G, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Lazy<? extends MonadT<F, G, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, MonadT<F, G, ?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<B, MonadT<F, G, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, B> discardL(Applicative<B, MonadT<F, G, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadT<F, G, A> discardR(Applicative<B, MonadT<F, G, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }
}
