package com.jnape.palatable.lambda.monad;

/**
 * A type into which a {@link Monad} is embedded whilst internally preserving the {@link Monad} structure.
 *
 * @param <M>  the {@link Monad} embedded in this {@link MonadBase}
 * @param <A>  the carrier type
 * @param <MB> the witness
 */
@SuppressWarnings("unused")
public interface MonadBase<M extends Monad<?, M>, A, MB extends MonadBase<?, ?, MB>> {

    /**
     * Lift a new argument {@link Monad} into this {@link MonadBase}.
     *
     * @param nc  the argument {@link Monad}
     * @param <C> the {@link Monad} carrier type
     * @param <N> the argument {@link Monad} witness
     * @return the new {@link MonadBase}
     */
    <C, N extends Monad<?, N>> MonadBase<N, C, MB> lift(Monad<C, N> nc);
}
