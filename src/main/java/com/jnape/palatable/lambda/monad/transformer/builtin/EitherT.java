package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link MonadT monad transformer} for {@link Either}.
 *
 * @param <M> the outer {@link Monad}
 * @param <L> the left type
 * @param <R> the right type
 */
public final class EitherT<M extends Monad<?, M>, L, R> implements MonadT<M, Either<L, ?>, R> {

    private final Monad<Either<L, R>, M> melr;

    private EitherT(Monad<Either<L, R>, M> melr) {
        this.melr = melr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <GA extends Monad<R, Either<L, ?>>, FGA extends Monad<GA, M>> FGA run() {
        return melr.<GA>fmap(Either::coerce).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> flatMap(Function<? super R, ? extends Monad<R2, MonadT<M, Either<L, ?>, ?>>> f) {
        return eitherT(melr.flatMap(lr -> lr.match(l -> melr.pure(left(l)),
                                                   r -> f.apply(r).<EitherT<M, L, R2>>coerce().run())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> pure(R2 r2) {
        return eitherT(melr.pure(right(r2)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> fmap(Function<? super R, ? extends R2> fn) {
        return MonadT.super.<R2>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> zip(
            Applicative<Function<? super R, ? extends R2>, MonadT<M, Either<L, ?>, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> Lazy<EitherT<M, L, R2>> lazyZip(
            Lazy<? extends Applicative<Function<? super R, ? extends R2>, MonadT<M, Either<L, ?>, ?>>> lazyAppFn) {
        return new Compose<>(melr)
                .lazyZip(lazyAppFn.fmap(maybeT -> new Compose<>(
                        maybeT.<EitherT<M, L, Function<? super R, ? extends R2>>>coerce()
                                .<Either<L, Function<? super R, ? extends R2>>,
                                        Monad<Either<L, Function<? super R, ? extends R2>>, M>>run())))
                .fmap(compose -> eitherT(compose.getCompose()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> discardL(Applicative<R2, MonadT<M, Either<L, ?>, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> EitherT<M, L, R> discardR(Applicative<B, MonadT<M, Either<L, ?>, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof EitherT<?, ?, ?> && Objects.equals(melr, ((EitherT<?, ?, ?>) other).melr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(melr);
    }

    @Override
    public String toString() {
        return "EitherT{melr=" + melr + '}';
    }

    /**
     * Static factory method for lifting a <code>{@link Monad}&lt;{@link Either}&lt;L, R&gt;, M&gt;</code> into an
     * {@link EitherT}.
     *
     * @param melr the {@link Monad}&lt;{@link Either}&lt;L, R&gt;, M&gt;
     * @param <M>  the outer {@link Monad} unification parameter
     * @param <L>  the left type
     * @param <R>  the right type
     * @return the {@link EitherT}
     */
    public static <M extends Monad<?, M>, L, R> EitherT<M, L, R> eitherT(Monad<Either<L, R>, M> melr) {
        return new EitherT<>(melr);
    }
}
