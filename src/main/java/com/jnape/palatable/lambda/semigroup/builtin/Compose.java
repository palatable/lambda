package com.jnape.palatable.lambda.semigroup.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.SemigroupFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.concurrent.CompletableFuture;

/**
 * A {@link Semigroup} instance formed by <code>{@link CompletableFuture}&lt;A&gt;</code> and a semigroup over
 * <code>A</code>. If either {@link CompletableFuture}s completes exceptionally, the result is also an exceptionally
 * completed future.
 * <p>
 * Note that this operation only takes as long as the slowest future to complete.
 * <p>
 * For the {@link Monoid}, see {@link com.jnape.palatable.lambda.monoid.builtin.Compose}.
 *
 * @param <A> the future parameter type
 */
public final class Compose<A> implements SemigroupFactory<Semigroup<A>, CompletableFuture<A>> {

    private static final Compose INSTANCE = new Compose();

    private Compose() {
    }

    @Override
    public Semigroup<CompletableFuture<A>> apply(Semigroup<A> aSemigroup) {
        return (futureX, futureY) -> futureX.thenCompose(x -> futureY.thenApply(y -> aSemigroup.apply(x, y)));
    }

    @SuppressWarnings("unchecked")
    public static <A> Compose<A> compose() {
        return INSTANCE;
    }

    public static <A> Semigroup<CompletableFuture<A>> compose(Semigroup<A> aSemigroup) {
        return Compose.<A>compose().apply(aSemigroup);
    }

    public static <A> Fn1<CompletableFuture<A>, CompletableFuture<A>> compose(Semigroup<A> aSemigroup,
                                                                              CompletableFuture<A> x) {
        return compose(aSemigroup).apply(x);
    }

    public static <A> CompletableFuture<A> compose(Semigroup<A> aSemigroup,
                                                   CompletableFuture<A> x,
                                                   CompletableFuture<A> y) {
        return compose(aSemigroup, x).apply(y);
    }
}
