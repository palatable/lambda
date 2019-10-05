package com.jnape.palatable.lambda.monad;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.recursion.Trampoline;
import com.jnape.palatable.lambda.functions.specialized.Lift;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.transformer.MonadT;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.Either.right;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;

/**
 * A stack-safe {@link MonadT monad transformer} that can safely interpret deeply nested left- or right-associated
 * binds for any {@link MonadRec}.
 * <p>
 * Example:
 * <pre>
 * {@code
 * Times.<Fn1<Integer, Integer>>times(100_000, f -> f.fmap(x -> x + 1), id()).apply(0); // stack-overflow
 * Times.<SafeT<Fn1<Integer, ?>, Integer>>times(100_000, f -> f.fmap(x -> x + 1), safeT(id()))
 *         .<Fn1<Integer, Integer>>runSafeT()
 *         .apply(0); // 100_000
 * }
 * </pre>
 * <p>
 * Inspired by Phil Freeman's paper
 * <a href="http://functorial.com/stack-safety-for-free/index.pdf">Stack Safety for Free</a>.
 *
 * @param <M> the {@link MonadRec} instance
 * @param <A> the carrier type
 */
public final class SafeT<M extends MonadRec<?, M>, A> implements
        MonadT<M, A, SafeT<M, ?>, SafeT<?, ?>> {

    private final Body<M, A> body;
    private final Pure<M>    pureM;

    private SafeT(Body<M, A> body, Pure<M> pureM) {
        this.body = body;
        this.pureM = pureM;
    }

    /**
     * Recover the full structure of the embedded {@link Monad} in a stack-safe way.
     *
     * @param <MA> the witnessed target type
     * @return the embedded {@link Monad}
     */
    public <MA extends MonadRec<A, M>> MA runSafeT() {
        return body.resume().match(
                fFree -> fFree.trampolineM(freeF -> freeF.resume().match(
                        monadRec -> monadRec.fmap(RecursiveResult::recurse),
                        a -> pureM.<A, MonadRec<A, M>>apply(a).fmap(RecursiveResult::terminate))).coerce(),
                pureM::apply);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> fmap(Fn1<? super A, ? extends B> fn) {
        return MonadT.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, N extends MonadRec<?, N>> SafeT<N, B> lift(MonadRec<B, N> nb) {
        return liftSafeT().apply(nb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> flatMap(Fn1<? super A, ? extends Monad<B, SafeT<M, ?>>> f) {
        return new SafeT<>(Body.suspend(body, a -> f.apply(a).<SafeT<M, B>>coerce().body), pureM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> zip(Applicative<Fn1<? super A, ? extends B>, SafeT<M, ?>> appFn) {
        return MonadT.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<SafeT<M, B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, SafeT<M, ?>>> lazyAppFn) {
        return MonadT.super.lazyZip(lazyAppFn).fmap(Applicative<B, SafeT<M, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> discardL(Applicative<B, SafeT<M, ?>> appB) {
        return MonadT.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, A> discardR(Applicative<B, SafeT<M, ?>> appB) {
        return MonadT.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> pure(B b) {
        return pureSafeT(pureM).apply(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> SafeT<M, B> trampolineM(Fn1<? super A, ? extends MonadRec<RecursiveResult<A, B>, SafeT<M, ?>>> bounce) {
        return flatMap(bounce.fmap(mab -> mab.flatMap(aOrB -> aOrB
                .match(a -> mab.pure(a).trampolineM(bounce), Pure.of(mab)::apply))));
    }

    /**
     * Lift any <code>{@link MonadRec MonadRec}&lt;A, M&gt;</code> into a <code>{@link SafeT SafeT}&lt;M, A&gt;</code>.
     *
     * @param ma  the {@link MonadRec MonadRec}&lt;A, M&gt;
     * @param <M> the {@link MonadRec} witness
     * @param <A> the carrier type
     * @return the new {@link SafeT}
     */
    public static <M extends MonadRec<?, M>, A> SafeT<M, A> safeT(MonadRec<A, M> ma) {
        return new SafeT<>(Body.more(ma.fmap(Body::done)), Pure.of(ma));
    }

    /**
     * The canonical {@link Pure} instance for {@link SafeT}.
     *
     * @param pureM the argument {@link Monad} {@link Pure}
     * @param <M>   the argument {@link Monad} witness
     * @return the {@link Pure} instance
     */
    public static <M extends MonadRec<?, M>> Pure<SafeT<M, ?>> pureSafeT(Pure<M> pureM) {
        return new Pure<SafeT<M, ?>>() {
            @Override
            public <A> SafeT<M, A> checkedApply(A a) throws Throwable {
                return safeT(pureM.<A, MonadRec<A, M>>apply(a));
            }
        };
    }

    /**
     * {@link Lift} for {@link SafeT}.
     *
     * @return the {@link Monad} lifted into {@link SafeT}
     */
    public static Lift<SafeT<?, ?>> liftSafeT() {
        return SafeT::safeT;
    }

    private abstract static class Body<M extends MonadRec<?, M>, A> implements
            CoProduct2<Either<MonadRec<Body<M, A>, M>, A>, Body.Suspended<M, ?, A>, Body<M, A>> {

        private Body() {
        }

        public abstract Either<MonadRec<Body<M, A>, M>, A> resume();

        private static <M extends MonadRec<?, M>, A> Body<M, A> done(A a) {
            return new Done<>(a);
        }

        private static <M extends MonadRec<?, M>, A> Body<M, A> more(MonadRec<Body<M, A>, M> mb) {
            return new More<>(mb);
        }

        private static <M extends MonadRec<?, M>, A, B> Body<M, B> suspend(Body<M, A> freeA, Fn1<A, Body<M, B>> fn) {
            return new SafeT.Body.Suspended<>(freeA, fn);
        }

        private static final class Done<M extends MonadRec<?, M>, A> extends Body<M, A> {
            private final A a;

            private Done(A a) {
                this.a = a;
            }

            @Override
            public <R> R match(Fn1<? super Either<MonadRec<Body<M, A>, M>, A>, ? extends R> aFn,
                               Fn1<? super SafeT.Body.Suspended<M, ?, A>, ? extends R> bFn) {
                return aFn.apply(right(a));
            }

            @Override
            public Either<MonadRec<Body<M, A>, M>, A> resume() {
                return right(a);
            }
        }

        private static final class More<M extends MonadRec<?, M>, A> extends Body<M, A> {
            private final MonadRec<Body<M, A>, M> mfa;

            private More(MonadRec<Body<M, A>, M> mfa) {
                this.mfa = mfa;
            }

            @Override
            public <R> R match(Fn1<? super Either<MonadRec<Body<M, A>, M>, A>, ? extends R> aFn,
                               Fn1<? super SafeT.Body.Suspended<M, ?, A>, ? extends R> bFn) {
                return aFn.apply(left(mfa));
            }

            @Override
            public Either<MonadRec<Body<M, A>, M>, A> resume() {
                return left(mfa);
            }
        }

        private static final class Suspended<M extends MonadRec<?, M>, A, B> extends Body<M, B> {
            private final Body<M, A>         source;
            private final Fn1<A, Body<M, B>> f;

            private Suspended(Body<M, A> source, Fn1<A, Body<M, B>> f) {
                this.source = source;
                this.f = f;
            }

            public Either<MonadRec<Body<M, B>, M>, B> resume() {
                Φ<M, B, RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>> phi =
                        new Φ<M, B, RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>>() {
                            @Override
                            public <Z> RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>> apply(
                                    Body<M, Z> source, Fn1<Z, Body<M, B>> f) {
                                return source.match(
                                        e -> e.match(more -> terminate(left(more.fmap(body -> suspend(body, f)))),
                                                     z -> recurse(f.apply(z))),
                                        associateRight(f));
                            }
                        };
                return Trampoline.<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>trampoline(
                        free -> free.match(RecursiveResult::terminate, suspended -> suspended.eliminate(phi)),
                        this);
            }

            @Override
            public <R> R match(Fn1<? super Either<MonadRec<Body<M, B>, M>, B>, ? extends R> aFn,
                               Fn1<? super Suspended<M, ?, B>, ? extends R> bFn) {
                return bFn.apply(this);
            }

            private <Z> Fn1<Suspended<M, ?, Z>, RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>>
            associateRight(Fn1<Z, Body<M, B>> f) {
                Φ<M, Z, RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>> phi =
                        new Φ<M, Z, RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>>>() {
                            @Override
                            public <Y> RecursiveResult<Body<M, B>, Either<MonadRec<Body<M, B>, M>, B>> apply(
                                    Body<M, Y> source,
                                    Fn1<Y, Body<M, Z>> g) {
                                return recurse(suspend(source, x -> suspend(g.apply(x), f)));
                            }
                        };

                return suspended -> suspended.eliminate(phi);
            }

            @SuppressWarnings("NonAsciiCharacters")
            private <R> R eliminate(Φ<M, B, R> Φ) {
                return Φ.apply(source, f);
            }

            @SuppressWarnings("NonAsciiCharacters")
            private interface Φ<M extends MonadRec<?, M>, B, R> {
                <A> R apply(Body<M, A> source, Fn1<A, Body<M, B>> fn);
            }
        }
    }
}