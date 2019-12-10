package com.jnape.palatable.lambda.monad;

/**
 * A type into which a {@link MonadRec} is embedded whilst internally preserving the {@link MonadRec} structure.
 *
 * @param <M>  the {@link MonadRec} embedded in this {@link MonadBase}
 * @param <A>  the carrier type
 * @param <MB> the witness
 */
@SuppressWarnings("unused")
public interface MonadBase<M extends MonadRec<?, M>, A, MB extends MonadBase<?, ?, MB>> {

    /**
     * Lift a new argument {@link MonadRec} into this {@link MonadBase}.
     *
     * @param nc  the argument {@link MonadRec}
     * @param <C> the {@link MonadRec} carrier type
     * @param <N> the argument {@link MonadRec} witness
     * @return the new {@link MonadBase}
     */
    <C, N extends MonadRec<?, N>> MonadBase<N, C, MB> lift(MonadRec<C, N> nc);
}
