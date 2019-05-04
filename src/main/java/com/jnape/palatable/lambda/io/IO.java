package com.jnape.palatable.lambda.io;

import com.jnape.palatable.lambda.adt.Try;
import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.Fn0.fn0;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.recurse;
import static com.jnape.palatable.lambda.functions.recursion.RecursiveResult.terminate;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;
import static com.jnape.palatable.lambda.functions.specialized.checked.CheckedSupplier.checked;
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
    public final IO<A> exceptionally(Function<? super Throwable, ? extends A> recoveryFn) {
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
    public final <B> IO<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <B> IO<B> zip(Applicative<Function<? super A, ? extends B>, IO<?>> appFn) {
        @SuppressWarnings("unchecked")
        IO<Object> source = (IO<Object>) this;
        @SuppressWarnings("unchecked")
        IO<Function<Object, Object>> zip = (IO<Function<Object, Object>>) (Object) appFn;
        return new Compose<>(source, a(zip));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<IO<B>> lazyZip(Lazy<? extends Applicative<Function<? super A, ? extends B>, IO<?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<B, IO<?>>::coerce);
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
    public final <B> IO<B> flatMap(Function<? super A, ? extends Monad<B, IO<?>>> f) {
        @SuppressWarnings("unchecked")
        IO<Object> source = (IO<Object>) this;
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Function<Object, IO<Object>> flatMap = (Function<Object, IO<Object>>) (Object) f;
        return new Compose<>(source, Choice2.b(flatMap));
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
     * @param supplier the lambda to coerce
     * @param <A>      the result type
     * @return the {@link IO}
     */
    public static <A> IO<A> io(Supplier<A> supplier) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return supplier.get();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return supplyAsync(supplier, executor);
            }
        };
    }

    /**
     * Static factory method for creating an {@link IO} that runs <code>runnable</code> and returns {@link Unit}.
     *
     * @param runnable the {@link Runnable}
     * @return the {@link IO}
     */
    public static IO<Unit> io(Runnable runnable) {
        return io(fn0(runnable));
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
    public static <A> IO<A> externallyManaged(Supplier<CompletableFuture<A>> supplier) {
        return new IO<A>() {
            @Override
            public A unsafePerformIO() {
                return checked(() -> unsafePerformAsyncIO().get()).get();
            }

            @Override
            public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
                return supplier.get();
            }
        };
    }

    private static final class Compose<A> extends IO<A> {
        private final IO<Object>                                                          source;
        private final Choice2<IO<Function<Object, Object>>, Function<Object, IO<Object>>> composition;

        private Compose(IO<Object> source,
                        Choice2<IO<Function<Object, Object>>, Function<Object, IO<Object>>> composition) {
            this.source = source;
            this.composition = composition;
        }

        @Override
        public A unsafePerformIO() {
            @SuppressWarnings("unchecked")
            A result = (A) trampoline(into((source, compositions) -> {
                Object res = source.unsafePerformIO();
                return compositions.isEmpty()
                       ? terminate(res)
                       : compositions.pop().match(
                               zip -> recurse(tuple(io(zip.unsafePerformIO().apply(res)), compositions)),
                               flatMap -> {
                                   IO<Object> next = flatMap.apply(res);
                                   return (next instanceof Compose<?>)
                                          ? recurse(((Compose<?>) next).deforest(compositions))
                                          : recurse(tuple(next, compositions));
                               });
            }), deforest(new LinkedList<>()));

            return result;
        }

        @Override
        public CompletableFuture<A> unsafePerformAsyncIO(Executor executor) {
            @SuppressWarnings("unchecked")
            CompletableFuture<A> future = (CompletableFuture<A>) deforest(new LinkedList<>())
                    .into((source, compositions) -> foldLeft(
                            (io, composition) -> composition
                                    .match(zip -> zip.unsafePerformAsyncIO(executor).thenCompose(io::thenApply),
                                           flatMap -> io.thenComposeAsync(obj -> flatMap.apply(obj)
                                                   .unsafePerformAsyncIO(executor))),
                            source.unsafePerformAsyncIO(executor),
                            compositions));
            return future;
        }

        private Tuple2<IO<Object>, LinkedList<Choice2<IO<Function<Object, Object>>, Function<Object, IO<Object>>>>>
        deforest(LinkedList<Choice2<IO<Function<Object, Object>>, Function<Object, IO<Object>>>> branches) {
            Tuple2<Compose<?>, LinkedList<Choice2<IO<Function<Object, Object>>, Function<Object, IO<Object>>>>> args =
                    tuple(this, branches);
            return trampoline(into((source, compositions) -> {
                IO<Object> leaf = source.source;
                compositions.push(source.composition);
                return leaf instanceof Compose<?>
                       ? recurse(tuple((Compose<?>) leaf, compositions))
                       : terminate(tuple(leaf, compositions));
            }), args);
        }

    }
}
