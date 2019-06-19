package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;

/**
 * A {@link MonadT monad transformer} for {@link Either}.
 *
 * @param <M> the outer {@link Monad}
 * @param <L> the left type
 * @param <R> the right type
 */
public final class EitherT<M extends Monad<?, M>, L, R> implements
        Bifunctor<L, R, EitherT<M, ?, ?>>,
        MonadT<M, Either<L, ?>, R, EitherT<M, L, ?>> {

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
    public <R2> EitherT<M, L, R2> flatMap(Fn1<? super R, ? extends Monad<R2, EitherT<M, L, ?>>> f) {
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
    public <R2> EitherT<M, L, R2> fmap(Fn1<? super R, ? extends R2> fn) {
        return MonadT.super.<R2>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> zip(
            Applicative<Fn1<? super R, ? extends R2>, EitherT<M, L, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> Lazy<EitherT<M, L, R2>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super R, ? extends R2>, EitherT<M, L, ?>>> lazyAppFn) {
        return new Compose<>(melr)
                .lazyZip(lazyAppFn.fmap(maybeT -> new Compose<>(
                        maybeT.<EitherT<M, L, Fn1<? super R, ? extends R2>>>coerce()
                                .<Either<L, Fn1<? super R, ? extends R2>>,
                                        Monad<Either<L, Fn1<? super R, ? extends R2>>, M>>run())))
                .fmap(compose -> eitherT(compose.getCompose()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> discardL(Applicative<R2, EitherT<M, L, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> EitherT<M, L, R> discardR(Applicative<B, EitherT<M, L, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <L2, R2> EitherT<M, L2, R2> biMap(Fn1<? super L, ? extends L2> lFn,
                                             Fn1<? super R, ? extends R2> rFn) {
        return eitherT(melr.fmap(e -> e.biMap(lFn, rFn)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <L2> EitherT<M, L2, R> biMapL(Fn1<? super L, ? extends L2> fn) {
        return (EitherT<M, L2, R>) Bifunctor.super.<L2>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> biMapR(Fn1<? super R, ? extends R2> fn) {
        return (EitherT<M, L, R2>) Bifunctor.super.<R2>biMapR(fn);
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
