package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.traversable.LambdaIterable;
import com.jnape.palatable.lambda.traversable.LambdaMap;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Map;

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
 * @param <AppA>    the Applicative instance wrapped in the input Traversable
 * @param <TravA>   the Traversable instance wrapped in the output Applicative
 * @param <AppTrav> the concrete parametrized output Applicative type
 * @param <TravApp> the concrete parametrized input Traversable type
 */
public final class Sequence<A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
        AppA extends Applicative<A, App>,
        TravA extends Traversable<A, Trav>,
        AppTrav extends Applicative<TravA, App>,
        TravApp extends Traversable<AppA, Trav>> implements Fn2<TravApp, Fn1<TravA, ? extends AppTrav>, AppTrav> {

    private static final Sequence<?, ?, ?, ?, ?, ?, ?> INSTANCE = new Sequence<>();

    private Sequence() {
    }

    @Override
    public AppTrav checkedApply(TravApp traversable, Fn1<TravA, ? extends AppTrav> pure) {
        return traversable.traverse(id(), pure);
    }

    @SuppressWarnings("unchecked")
    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            AppA extends Applicative<A, App>,
            TravA extends Traversable<A, Trav>,
            AppTrav extends Applicative<TravA, App>,
            TravApp extends Traversable<AppA, Trav>> Sequence<A, App, Trav, AppA, TravA, AppTrav, TravApp> sequence() {
        return (Sequence<A, App, Trav, AppA, TravA, AppTrav, TravApp>) INSTANCE;
    }

    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            AppA extends Applicative<A, App>,
            TravA extends Traversable<A, Trav>,
            AppTrav extends Applicative<TravA, App>,
            TravApp extends Traversable<AppA, Trav>> Fn1<Fn1<TravA, ? extends AppTrav>, AppTrav> sequence(
            TravApp traversable) {
        return Sequence.<A, App, Trav, AppA, TravA, AppTrav, TravApp>sequence().apply(traversable);
    }

    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            TravA extends Traversable<A, Trav>,
            AppA extends Applicative<A, App>,
            AppTrav extends Applicative<TravA, App>,
            TravApp extends Traversable<AppA, Trav>> AppTrav sequence(TravApp traversable,
                                                                      Fn1<TravA, ? extends AppTrav> pure) {
        return Sequence.<A, App, Trav, AppA, TravA, AppTrav, TravApp>sequence(traversable).apply(pure);
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    public static <A, App extends Applicative<?, App>, AppA extends Applicative<A, App>,
            AppIterable extends Applicative<Iterable<A>, App>,
            IterableApp extends Iterable<AppA>>
    Fn1<Fn1<Iterable<A>, ? extends AppIterable>, AppIterable> sequence(IterableApp iterableApp) {
        return pure -> (AppIterable) Sequence.<A, App, LambdaIterable<?>, LambdaIterable<A>, AppA, Applicative<LambdaIterable<A>, App>, LambdaIterable<AppA>>sequence(
                LambdaIterable.wrap(iterableApp), x -> pure.apply(x.unwrap()).fmap(LambdaIterable::wrap))
                .fmap(LambdaIterable::unwrap);
    }

    public static <A, App extends Applicative<?, App>, AppA extends Applicative<A, App>,
            AppIterable extends Applicative<Iterable<A>, App>, IterableApp extends Iterable<AppA>>
    AppIterable sequence(IterableApp iterableApp, Fn1<Iterable<A>, ? extends AppIterable> pure) {
        return Sequence.<A, App, AppA, AppIterable, IterableApp>sequence(iterableApp).apply(pure);
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    public static <A, B, App extends Applicative<?, App>, AppB extends Applicative<B, App>,
            AppMap extends Applicative<Map<A, B>, App>, MapApp extends Map<A, AppB>>
    Fn1<Fn1<Map<A, B>, ? extends AppMap>, AppMap> sequence(MapApp mapApp) {
        return pure -> (AppMap) Sequence.<B, App, LambdaMap<A, ?>, LambdaMap<A, B>, AppB, Applicative<LambdaMap<A, B>, App>, LambdaMap<A, AppB>>sequence(
                LambdaMap.wrap(mapApp), x -> pure.apply(x.unwrap()).fmap(LambdaMap::wrap))
                .fmap(LambdaMap::unwrap);
    }

    public static <A, B, App extends Applicative<?, App>, AppB extends Applicative<B, App>,
            AppMap extends Applicative<Map<A, B>, App>, MapApp extends Map<A, AppB>>
    AppMap sequence(MapApp mapApp, Fn1<Map<A, B>, ? extends AppMap> pure) {
        return Sequence.<A, B, App, AppB, AppMap, MapApp>sequence(mapApp).apply(pure);
    }
}
