package com.jnape.palatable.lambda.monoid.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.MonoidFactory;
import com.jnape.palatable.lambda.monoid.Monoid;
import com.jnape.palatable.lambda.semigroup.Semigroup;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.jnape.palatable.lambda.monoid.Monoid.monoid;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * A {@link Monoid} instance formed by <code>{@link CompletableFuture}&lt;A&gt;</code> and a monoid over
 * <code>A</code>. If either {@link CompletableFuture}s completes exceptionally, the result is also an exceptionally
 * completed future.
 * <p>
 * Note that this operation only takes as long as the slowest future to complete.
 * <p>
 * For the {@link Semigroup}, see {@link com.jnape.palatable.lambda.semigroup.builtin.Compose}.
 *
 * @param <A> the future parameter type
 */
public final class Compose<A> implements MonoidFactory<Monoid<A>, CompletableFuture<A>> {

    private static final Compose INSTANCE = new Compose();

    private Compose() {
    }

    @Override
    public Monoid<CompletableFuture<A>> apply(Monoid<A> aMonoid) {
        return monoid(com.jnape.palatable.lambda.semigroup.builtin.Compose.compose(aMonoid),
                      (Supplier<CompletableFuture<A>>) () -> completedFuture(aMonoid.identity()));
    }

    @SuppressWarnings("unchecked")
    public static <A> Compose<A> compose() {
        return INSTANCE;
    }

    public static <A> Monoid<CompletableFuture<A>> compose(Monoid<A> aMonoid) {
        return Compose.<A>compose().apply(aMonoid);
    }

    public static <A> Fn1<CompletableFuture<A>, CompletableFuture<A>> compose(Monoid<A> aMonoid,
                                                                              CompletableFuture<A> x) {
        return compose(aMonoid).apply(x);
    }

    public static <A> CompletableFuture<A> compose(Monoid<A> aMonoid,
                                                   CompletableFuture<A> x,
                                                   CompletableFuture<A> y) {
        return compose(aMonoid, x).apply(y);
    }
}
