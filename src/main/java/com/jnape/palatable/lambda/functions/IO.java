package com.jnape.palatable.lambda.functions;

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
 * A {@link Monad} representing some effectful computation to be performed.
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
     * @return the {@link CompletableFuture} representing this {@link IO}'s eventual result
     * @see IO#unsafePerformAsyncIO()
     */
    default CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
        return supplyAsync(this::unsafePerformIO, executor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <B> IO<B> flatMap(Function<? super A, ? extends Monad<B, IO<?>>> f) {
        return () -> f.apply(unsafePerformIO()).<IO<B>>coerce().unsafePerformIO();
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
        return Monad.super.zip(appFn).coerce();
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
