package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.traversable.LambdaIterable;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Optional;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * Given a {@link Traversable} of {@link Applicative}s and a pure {@link Applicative} constructor, traverse the
 * elements from left to right, zipping the {@link Applicative}s together and collecting the results. If the
 * {@link Traversable} is empty, simply wrap it in the {@link Applicative} by calling <code>pure</code>.
 * <p>
 * Modulo any type-level coercion, this is equivalent to <code>traversable.traverse(id(), pure)</code>.
 * <p>
 * Note that specialized overloads exist for certain built-in JDK types that would otherwise be instances
 * {@link Traversable} if it weren't for the extensibility problem.
 *
 * @param <A>       the Traversable element type
 * @param <App>     the Applicative unification parameter
 * @param <Trav>    the Traversable unification parameter
 * @param <AppTrav> the concrete parametrized output Applicative type
 * @param <TravApp> the concrete parametrized input Traversable type
 */
public final class Sequence<A, App extends Applicative, Trav extends Traversable, AppTrav extends Applicative<? extends Traversable<A, Trav>, App>,
        TravApp extends Traversable<? extends Applicative<A, App>, Trav>> implements Fn2<TravApp, Function<? super Traversable<A, Trav>, ? extends AppTrav>, AppTrav> {

    private static final Sequence INSTANCE = new Sequence();

    private Sequence() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppTrav apply(TravApp traversable, Function<? super Traversable<A, Trav>, ? extends AppTrav> pure) {
        return (AppTrav) traversable.traverse(id(), pure);
    }

    @SuppressWarnings("unchecked")
    public static <A, App extends Applicative, Trav extends Traversable,
            AppTrav extends Applicative<? extends Traversable<A, Trav>, App>,
            TravApp extends Traversable<? extends Applicative<A, App>, Trav>> Sequence<A, App, Trav, AppTrav, TravApp> sequence() {
        return INSTANCE;
    }

    public static <A, App extends Applicative, Trav extends Traversable,
            AppTrav extends Applicative<? extends Traversable<A, Trav>, App>,
            TravApp extends Traversable<? extends Applicative<A, App>, Trav>> Fn1<Function<? super Traversable<A, Trav>, ? extends AppTrav>, AppTrav> sequence(
            TravApp traversable) {
        return Sequence.<A, App, Trav, AppTrav, TravApp>sequence().apply(traversable);
    }

    public static <A, App extends Applicative, Trav extends Traversable,
            AppTrav extends Applicative<? extends Traversable<A, Trav>, App>,
            TravApp extends Traversable<? extends Applicative<A, App>, Trav>> AppTrav sequence(TravApp traversable,
                                                                                               Function<? super Traversable<A, Trav>, ? extends AppTrav> pure) {
        return Sequence.<A, App, Trav, AppTrav, TravApp>sequence(traversable).apply(pure);
    }

    @SuppressWarnings("unchecked")
    public static <A, App extends Applicative, AppIterable extends Applicative<Iterable<A>, App>, IterableApp extends Iterable<? extends Applicative<A, App>>> Fn1<Function<? super Iterable<A>, ? extends AppIterable>, AppIterable> sequence(
            IterableApp iterableApp) {
        return pure ->
                (AppIterable) sequence(LambdaIterable.wrap(iterableApp), x -> pure.apply(((LambdaIterable<A>) x).unwrap())
                        .fmap(LambdaIterable::wrap))
                        .fmap(LambdaIterable::unwrap);
    }

    /**
     * @deprecated in favor of wrapping the {@link Optional} in {@link Maybe}, then sequencing
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <A, App extends Applicative, AppOptional extends Applicative<Optional<A>, App>, OptionalApp extends Optional<? extends Applicative<A, App>>> Fn1<Function<? super Optional<A>, ? extends AppOptional>, AppOptional> sequence(
            OptionalApp optionalApp) {
        return pure -> (AppOptional) sequence(Maybe.fromOptional(optionalApp), x -> pure.apply(((Maybe<A>) x).toOptional())
                .fmap(Maybe::fromOptional))
                .fmap(Maybe::toOptional);
    }

    public static <A, App extends Applicative, AppIterable extends Applicative<Iterable<A>, App>,
            IterableApp extends Iterable<? extends Applicative<A, App>>> AppIterable sequence(IterableApp iterableApp,
                                                                                              Function<? super Iterable<A>, ? extends AppIterable> pure) {
        return Sequence.<A, App, AppIterable, IterableApp>sequence(iterableApp).apply(pure);
    }

    /**
     * @deprecated in favor of wrapping the {@link Optional} in {@link Maybe}, then sequencing
     */
    @Deprecated
    public static <A, App extends Applicative, AppOptional extends Applicative<Optional<A>, App>,
            OptionalApp extends Optional<? extends Applicative<A, App>>> AppOptional sequence(OptionalApp optionalApp,
                                                                                              Function<? super Optional<A>, ? extends AppOptional> pure) {
        return Sequence.<A, App, AppOptional, OptionalApp>sequence(optionalApp).apply(pure);
    }
}
