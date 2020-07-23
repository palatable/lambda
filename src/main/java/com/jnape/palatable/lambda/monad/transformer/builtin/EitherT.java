package com.jnape.palatable.lambda.monad.transformer.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Compose;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadError;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;

/**
 * A {@link MonadT monad transformer} for {@link Either}.
 *
 * @param <M> the outer {@link Monad stack-safe monad}
 * @param <L> the left type
 * @param <R> the right type
 */
public final class EitherT<M extends MonadRec<?, M>, L, R> implements
        Bifunctor<L, R, EitherT<M, ?, ?>>,
        MonadT<M, R, EitherT<M, L, ?>, EitherT<?, L, ?>>,
        MonadError<L, R, EitherT<M, L, ?>> {

    private final MonadRec<Either<L, R>, M> melr;

    private EitherT(MonadRec<Either<L, R>, M> melr) {
        this.melr = melr;
    }

    /**
     * Recover the full structure of the embedded {@link Monad}.
     *
     * @param <MELR> the witnessed target type
     * @return the embedded {@link Monad}
     */
    public <MELR extends MonadRec<Either<L, R>, M>> MELR runEitherT() {
        return melr.coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2, N extends MonadRec<?, N>> EitherT<N, L, R2> lift(MonadRec<R2, N> mb) {
        return EitherT.<L>liftEitherT().apply(mb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> flatMap(Fn1<? super R, ? extends Monad<R2, EitherT<M, L, ?>>> f) {
        return eitherT(melr.flatMap(lr -> lr.match(l -> melr.pure(left(l)),
                                                   r -> f.apply(r).<EitherT<M, L, R2>>coerce().runEitherT())));
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
        return eitherT(new Compose<>(this.<MonadRec<Either<L, R>, M>>runEitherT()).zip(
                new Compose<>(appFn.<EitherT<M, L, Fn1<? super R, ? extends R2>>>coerce()
                                      .<MonadRec<Either<L, Fn1<? super R, ? extends R2>>, M>>runEitherT()))
                               .getCompose());
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
                                .<MonadRec<Either<L, Fn1<? super R, ? extends R2>>, M>>runEitherT())))
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
    public EitherT<M, L, R> throwError(L l) {
        return eitherT(melr.pure(left(l)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EitherT<M, L, R> catchError(Fn1<? super L, ? extends Monad<R, EitherT<M, L, ?>>> recoveryFn) {
        return eitherT(runEitherT().flatMap(e -> e.match(
                l -> recoveryFn.apply(l).<EitherT<M, L, R>>coerce().runEitherT(),
                r -> melr.pure(r).fmap(Either::right))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R2> EitherT<M, L, R2> trampolineM(
            Fn1<? super R, ? extends MonadRec<RecursiveResult<R, R2>, EitherT<M, L, ?>>> fn) {
        return eitherT(runEitherT().trampolineM(lOrR -> lOrR
                .match(l -> melr.pure(terminate(left(l))),
                       r -> fn.apply(r).<EitherT<M, L, RecursiveResult<R, R2>>>coerce()
                               .runEitherT()
                               .fmap(lOrRR -> lOrRR.match(l -> terminate(left(l)),
                                                          rr -> rr.biMap(Either::right, Either::right))))));
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
    public static <M extends MonadRec<?, M>, L, R> EitherT<M, L, R> eitherT(MonadRec<Either<L, R>, M> melr) {
        return new EitherT<>(melr);
    }

    /**
     * The canonical {@link Pure} instance for {@link EitherT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <M>   the argument {@link Monad} witness
     * @param <L>   the left type
     * @return the {@link Pure} instance
     */
    public static <M extends MonadRec<?, M>, L> Pure<EitherT<M, L, ?>> pureEitherT(Pure<M> pureM) {
        return new Pure<EitherT<M, L, ?>>() {
            @Override
            public <R> EitherT<M, L, R> checkedApply(R r) throws Throwable {
                return eitherT(pureM.<R, MonadRec<R, M>>apply(r).fmap(Either::right));
            }
        };
    }

    /**
     * {@link Lift} for {@link EitherT}.
     *
     * @param <L> the left type
     * @return the {@link Monad}lifted into {@link EitherT}
     */
    public static <L> Lift<EitherT<?, L, ?>> liftEitherT() {
        return new Lift<EitherT<?, L, ?>>() {
            @Override
            public <A, M extends MonadRec<?, M>> EitherT<M, L, A> checkedApply(MonadRec<A, M> ga) {
                return eitherT(ga.fmap(Either::right));
            }
        };
    }

}
