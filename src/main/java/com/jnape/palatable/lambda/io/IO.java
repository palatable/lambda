package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Try;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.functions.Fn0;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.LazyRec;
import com.jnape.palatable.lambda.functions.specialized.SideEffect;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Downcast.downcast;
import static com.jnape.palatable.lambda.functor.builtin.Lazy.lazy;
import static com.jnape.palatable.lambda.monad.Monad.join;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.ForkJoinPool.commonPool;

/**
 * A {@link Monad} representing some side-effecting computation to be performed. Note that because {@link IO} inherently
 * offers an interface supporting parallelism, the optimal execution strategy for any given {@link IO} is encoded in
 * its composition.
 *
 * @param <A> the result type
 */
public abstract class IO<A> implements Monad<A, IO<?>> {

    private IO() {
    }

    /**
     * Run the effect represented by this {@link IO} instance, blocking the current thread until the effect terminates.
     *
     * @return the result of the effect
     */
    public abstract A unsafePerformIO();

    /**
     * Returns a {@link CompletableFuture} representing the result of this eventual effect. By default, this will
     * immediately run the effect in terms of the implicit {@link Executor} available to {@link CompletableFuture}
     * (usually the {@link java.util.concurrent.ForkJoinPool}). Note that specific {@link IO} constructions may allow
     * this method to delegate to externally-managed {@link CompletableFuture} instead of synthesizing their own.
     *
     * @return the {@link CompletableFuture} representing this {@link IO}'s eventual result
     * @see IO#unsafePerformAsyncIO(Executor)
     */
    public final CompletableFuture<A> unsafePerformAsyncIO() {
        return unsafePerformAsyncIO(commonPool());
    }

    /**
     * Returns a {@link CompletableFuture} representing the result of this eventual effect. By default, this will
     * immediately run the effect in terms of the provided {@link Executor}. Note that specific {@link IO}
     * constructions may allow this method to delegate to externally-managed {@link CompletableFuture} instead of
     * synthesizing their own.
     *
     * @param executor the {@link Executor} to run the {@link CompletableFuture} from
     * @return the {@link CompletableFuture} representing this {@link IO}'s eventual result
     * @see IO#unsafePerformAsyncIO()
     */
    public abstract CompletableFuture<A> unsafePerformAsyncIO(Executor executor);

    /**
     * Given a function from any {@link Throwable} to the result type <code>A</code>, if this {@link IO} successfully
     * yields a result, return it; otherwise, map the {@link Throwable} to the result type and return that.
     *
     * @param recoveryFn the recovery function
     * @return the guarded {@link IO}
     */
    public final IO<A> exceptionally(Fn1<? super Throwable, ? extends A> recoveryFn) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return Try.trying(IO.this::unsafePerformIO).recover(recoveryFn);
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return IO.this.unsafePerformAsyncIO(executor).exceptionally(recoveryFn::apply);
            }
        };
    }

    /**
     * Return an {@link IO} that will run <code>ensureIO</code> strictly after running this {@link IO} regardless of
     * whether this {@link IO} terminates normally, analogous to a finally block.
     *
     * @param ensureIO the {@link IO} to ensure runs strictly after this {@link IO}
     * @return the combined {@link IO}
     */
    public final IO<A> ensuring(IO<?> ensureIO) {
        return join(fmap(a -> ensureIO.fmap(constantly(a)))
                            .exceptionally(t -> join(ensureIO.<IO<A>>fmap(constantly(io(() -> {throw t;})))
                                                             .exceptionally(t2 -> io(() -> {
                                                                 t.addSuppressed(t2);
                                                                 throw t;
                                                             })))));
    }

    /**
     * Return a safe {@link IO} that will never throw by lifting the result of this {@link IO} into {@link Either},
     * catching any {@link Throwable} and wrapping it in a {@link Either#left(Object) left}.
     *
     * @return the safe {@link IO}
     */
    public final IO<Either<Throwable, A>> safe() {
        return fmap(Either::<Throwable, A>right).exceptionally(Either::left);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<B> pure(B b) {
        return io(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<B> fmap(Fn1<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> IO<B> zip(Applicative<Fn1<? super A, ? extends B>, IO<?>> appFn) {
        return new Compose<>(this, a((IO<Fn1<? super A, ? extends B>>) appFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> Lazy<IO<B>> lazyZip(Lazy<? extends Applicative<Fn1<? super A, ? extends B>, IO<?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).<IO<B>>fmap(Functor::coerce).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<B> discardL(Applicative<B, IO<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<A> discardR(Applicative<B, IO<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<B> flatMap(Fn1<? super A, ? extends Monad<B, IO<?>>> f) {
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Choice2<IO<?>, Fn1<Object, IO<?>>> composition = Choice2.b((Fn1<Object, IO<?>>) (Object) f);
        return new Compose<>(this, composition);
    }

    /**
     * Produce an {@link IO} that throws the given {@link Throwable} when executed.
     *
     * @param t   the {@link Throwable}
     * @param <A> any result type
     * @return the {@link IO}
     */
    public static <A> IO<A> throwing(Throwable t) {
        return io(() -> {throw t;});
    }

    /**
     * Static factory method for creating an {@link IO} that just returns <code>a</code> when performed.
     *
     * @param a   the result
     * @param <A> the result type
     * @return the {@link IO}
     */
    public static <A> IO<A> io(A a) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return a;
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return completedFuture(a);
            }
        };
    }

    /**
     * Static factory method for coercing a lambda to an {@link IO}.
     *
     * @param fn0 the lambda to coerce
     * @param <A> the result type
     * @return the {@link IO}
     */
    public static <A> IO<A> io(Fn0<? extends A> fn0) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return fn0.apply();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return supplyAsync(fn0::apply, executor);
            }
        };
    }

    /**
     * Static factory method for creating an {@link IO} that runs a {@link SideEffect} and returns {@link Unit}.
     *
     * @param sideEffect the {@link SideEffect}
     * @return the {@link IO}
     */
    public static IO<Unit> io(SideEffect sideEffect) {
        return io(fn0(() -> {
            sideEffect.Ω();
            return UNIT;
        }));
    }

    /**
     * Static factory method for creating an {@link IO} from an externally managed source of
     * {@link CompletableFuture completable futures}.
     * <p>
     * Note that constructing an {@link IO} this way results in no intermediate futures being constructed by either
     * {@link IO#unsafePerformAsyncIO()} or {@link IO#unsafePerformAsyncIO(Executor)}, and {@link IO#unsafePerformIO()}
     * is synonymous with invoking {@link CompletableFuture#get()} on the externally managed future.
     *
     * @param supplier the source of externally managed {@link CompletableFuture completable futures}
     * @param <A>      the result type
     * @return the {@link IO}
     */
    public static <A> IO<A> externallyManaged(Fn0<CompletableFuture<A>> supplier) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return fn0(() -> unsafePerformAsyncIO().get()).apply();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return supplier.apply();
            }
        };
    }

    private static final class Compose<A> extends IO<A> {

        private final IO<?>                              source;
        private final Choice2<IO<?>, Fn1<Object, IO<?>>> composition;

        private Compose(IO<?> source, Choice2<IO<?>, Fn1<Object, IO<?>>> composition) {
            this.source = source;
            this.composition = composition;
        }

        @Override
        public A unsafePerformIO() {
            Lazy<Object> lazyA = LazyRec.<IO<?>, Object>lazyRec(
                    (f, io) -> {
                        if (io instanceof IO.Compose<?>) {
                            Compose<?>   compose = (Compose<?>) io;
                            Lazy<Object> head    = f.apply(compose.source);
                            return compose.composition
                                    .match(zip -> head.flatMap(x -> f.apply(zip)
                                                   .<Fn1<Object, Object>>fmap(downcast())
                                                   .fmap(g -> g.apply(x))),
                                           flatMap -> head.fmap(flatMap).flatMap(f));
                        }
                        return lazy(io::unsafePerformIO);
                    },
                    this);
            return downcast(lazyA.value());
        }

        @Override
        @SuppressWarnings("unchecked")
        public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
            Lazy<CompletableFuture<Object>> lazyFuture = LazyRec.<IO<?>, CompletableFuture<Object>>lazyRec(
                    (f, io) -> {
                        if (io instanceof IO.Compose<?>) {
                            Compose<?>                      compose = (Compose<?>) io;
                            Lazy<CompletableFuture<Object>> head    = f.apply(compose.source);
                            return compose.composition
                                    .match(zip -> head.flatMap(futureX -> f.apply(zip)
                                                   .fmap(futureF -> futureF.thenCompose(f2 -> futureX
                                                           .thenApply(((Fn1<Object, Object>) f2).toFunction())))),
                                           flatMap -> head.fmap(futureX -> futureX
                                                   .thenComposeAsync(x -> f.apply(flatMap.apply(x)).value(),
                                                                     executor)));
                        }
                        return lazy(() -> (CompletableFuture<Object>) io.unsafePerformAsyncIO(executor));
                    },
                    this);

            return (CompletableFuture<A>) lazyFuture.value();
        }
    }
}
