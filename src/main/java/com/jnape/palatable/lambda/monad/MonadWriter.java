package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;

/**
 * A {@link Monad} that is capable of writing and accumulating state alongside a value, but is not necessarily capable
 * of simultaneously accessing the state and the value.
 *
 * @param <W>  the accumulation type
 * @param <A>  the output type
 * @param <MW> the witness
 */
public interface MonadWriter<W, A, MW extends MonadWriter<W, ?, MW>> extends Monad<A, MW> {

    /**
     * Map the accumulation into a value and pair it with the current output.
     *
     * @param fn  the mapping function
     * @param <B> the mapped output
     * @return the updated {@link MonadWriter}
     */
    <B> MonadWriter<W, Tuple2<A, B>, MW> listens(Fn1<? super W, ? extends B> fn);

    /**
     * Update the accumulated state.
     *
     * @param fn the update function
     * @return the updated {@link MonadWriter}
     */
    MonadWriter<W, A, MW> censor(Fn1<? super W, ? extends W> fn);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadWriter<W, B, MW> flatMap(Fn1<? super A, ? extends Monad<B, MW>> f);

    /**
     * {@inheritDoc}
     */
    @Override
    <B> MonadWriter<W, B, MW> pure(B b);

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadWriter<W, B, MW> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadWriter<W, B, MW> zip(Applicative<Fn1<? super A, ? extends B>, MW> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> Lazy<? extends MonadWriter<W, B, MW>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, MW>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<B, MW>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadWriter<W, B, MW> discardL(Applicative<B, MW> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> MonadWriter<W, A, MW> discardR(Applicative<B, MW> appB) {
        return Monad.super.discardR(appB).coerce();
    }
}
