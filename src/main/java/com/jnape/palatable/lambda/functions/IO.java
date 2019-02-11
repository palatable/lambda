package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.adt.Try;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier.checked;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A {@link Monad} representing some side-effecting computation to be performed. Note that because {@link IO} inherently
 * offers an interface supporting parallelism, the optimal execution strategy for any given {@link IO} is encoded in
 * its composition.
 *
 * @param <A> the result type
 */
public interface IO<A> extends Monad<A, IO<?>> {

    /**
     * Run the effect represented by this {@link IO} instance, blocking the current thread until the effect terminates.
     *
     * @return the result of the effect
     */
    A unsafePerformIO();

    /**
     * Returns a {@link CompletableFuture} representing the result of this eventual effect. By default, this will
     * immediately run the effect in terms of the implicit {@link Executor} available to {@link CompletableFuture}
     * (usually the {@link java.util.concurrent.ForkJoinPool}). Note that specific {@link IO} constructions may allow
     * this method to delegate to externally-managed {@link CompletableFuture} instead of synthesizing their own.
     *
     * @return the {@link CompletableFuture} representing this {@link IO}'s eventual result
     * @see IO#unsafePerformAsyncIO(Executor)
     */
    default CompletableFuture<A> unsafePerformAsyncIO() {
        return supplyAsync(this::unsafePerformIO);
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
    default CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
        return supplyAsync(this::unsafePerformIO, executor);
    }

    /**
     * Given a function from any {@link Throwable} to the result type <code>A</code>, if this {@link IO} successfully
     * yields a result, return it; otherwise, map the {@link Throwable} to the result type and return that.
     *
     * @param recoveryFn the recovery function
     * @return the guarded {@link IO}
     */
    default IO<A> exceptionally(Function<? super Throwable, ? extends A> recoveryFn) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return Try.trying(IO.this::unsafePerformIO).recover(recoveryFn);
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO() {
                return IO.this.unsafePerformAsyncIO().exceptionally(recoveryFn::apply);
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return IO.this.unsafePerformAsyncIO(executor).exceptionally(recoveryFn::apply);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> flatMap(Function<? super A, ? extends Monad<B, IO<?>>> f) {
        return new IO<B>() {
            @Override
            public B unsafePerformIO() {
                return f.apply(IO.this.unsafePerformIO()).<IO<B>>coerce().unsafePerformIO();
            }

            @Override
            public CompletableFuture<B> unsafePerformAsyncIO() {
                return IO.this.unsafePerformAsyncIO()
                        .thenCompose(a -> f.apply(a).<IO<B>>coerce().unsafePerformAsyncIO());
            }

            @Override
            public CompletableFuture<B> unsafePerformAsyncIO(Executor executor) {
                return IO.this.unsafePerformAsyncIO(executor)
                        .thenCompose(a -> f.apply(a).<IO<B>>coerce().unsafePerformAsyncIO(executor));
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> pure(B b) {
        return () -> b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> zip(Applicative<Function<? super A, ? extends B>, IO<?>> appFn) {
        IO<A> ioA = this;
        IO<Function<? super A, ? extends B>> ioF = appFn.coerce();
        return new IO<B>() {
            @Override
            public B unsafePerformIO() {
                return ioF.unsafePerformIO().apply(ioA.unsafePerformIO());
            }

            @Override
            public CompletableFuture<B> unsafePerformAsyncIO() {
                return ioF.unsafePerformAsyncIO().thenCompose(ioA.unsafePerformAsyncIO()::thenApply);
            }

            @Override
            public CompletableFuture<B> unsafePerformAsyncIO(Executor executor) {
                return ioF.unsafePerformAsyncIO(executor).thenCompose(ioA.unsafePerformAsyncIO(executor)::thenApply);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> discardL(Applicative<B, IO<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<A> discardR(Applicative<B, IO<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * Static factory method for coercing a lambda to an {@link IO}.
     *
     * @param io  the lambda to coerce
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(IO<A> io) {
        return io;
    }

    /**
     * Static factory method for creating an {@link IO} that just returns <code>a</code> when performed.
     *
     * @param a   the result
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(A a) {
        return io(() -> a);
    }

    /**
     * Static factory method for creating an {@link IO} that runs <code>runnable</code> and returns {@link Unit}.
     *
     * @param runnable the {@link Runnable}
     * @return the {@link IO}
     */
    static IO<Unit> io(Runnable runnable) {
        return io(() -> {
            runnable.run();
            return UNIT;
        });
    }

    /**
     * Static factory method for creating an {@link IO} from an <code>{@link Fn1}&lt;{@link Unit}, A&gt;</code>.
     *
     * @param fn1 the {@link Fn1}
     * @param <A> the result type
     * @return the {@link IO}
     */
    static <A> IO<A> io(Fn1<Unit, A> fn1) {
        return io(() -> fn1.apply(UNIT));
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
    static <A> IO<A> externallyManaged(Supplier<CompletableFuture<A>> supplier) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return checked(() -> unsafePerformAsyncIO().get()).get();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO() {
                return supplier.get();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return supplier.get();
            }
        };
    }
}
