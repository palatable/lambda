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
 * @param <TravA>   the Traversable instance wrapped in the output Applicative
 * @param <AppTrav> the concrete parametrized output Applicative type
 */
public final class Sequence<A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
        TravA extends Traversable<A, Trav>,
        AppTrav extends Applicative<TravA, App>> implements
        Fn2<Traversable<? extends Applicative<A, App>, Trav>, Fn1<TravA, ? extends AppTrav>, AppTrav> {

    private static final Sequence<?, ?, ?, ?, ?> INSTANCE = new Sequence<>();

    private Sequence() {
    }

    @Override
    public AppTrav checkedApply(Traversable<? extends Applicative<A, App>, Trav> traversable,
                                Fn1<TravA, ? extends AppTrav> pure) {
        return traversable.traverse(id(), pure);
    }

    @SuppressWarnings("unchecked")
    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            TravA extends Traversable<A, Trav>,
            AppTrav extends Applicative<TravA, App>> Sequence<A, App, Trav, TravA, AppTrav> sequence() {
        return (Sequence<A, App, Trav, TravA, AppTrav>) INSTANCE;
    }

    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            TravA extends Traversable<A, Trav>,
            AppTrav extends Applicative<TravA, App>> Fn1<Fn1<TravA, ? extends AppTrav>, AppTrav> sequence(
            Traversable<? extends Applicative<A, App>, Trav> traversable) {
        return Sequence.<A, App, Trav, TravA, AppTrav>sequence().apply(traversable);
    }

    public static <A, App extends Applicative<?, App>, Trav extends Traversable<?, Trav>,
            TravA extends Traversable<A, Trav>,
            AppTrav extends Applicative<TravA, App>> AppTrav sequence(
            Traversable<? extends Applicative<A, App>, Trav> traversable,
            Fn1<TravA, ? extends AppTrav> pure) {
        return Sequence.<A, App, Trav, TravA, AppTrav>sequence(traversable).apply(pure);
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    public static <A, App extends Applicative<?, App>, AppIterable extends Applicative<Iterable<A>, App>>
    Fn1<Fn1<Iterable<A>, ? extends AppIterable>, AppIterable> sequence(
            Iterable<? extends Applicative<A, App>> iterableApp) {
        return pure -> (AppIterable) Sequence.<A, App, LambdaIterable<?>, LambdaIterable<A>, Applicative<LambdaIterable<A>, App>>sequence(
                LambdaIterable.wrap(iterableApp), x -> pure.apply(x.unwrap()).fmap(LambdaIterable::wrap))
                .fmap(LambdaIterable::unwrap);
    }

    public static <A, App extends Applicative<?, App>, AppIterable extends Applicative<Iterable<A>, App>>
    AppIterable sequence(Iterable<? extends Applicative<A, App>> iterableApp,
                         Fn1<Iterable<A>, ? extends AppIterable> pure) {
        return Sequence.<A, App, AppIterable>sequence(iterableApp).apply(pure);
    }

    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    public static <A, B, App extends Applicative<?, App>, AppMap extends Applicative<Map<A, B>, App>>
    Fn1<Fn1<Map<A, B>, ? extends AppMap>, AppMap> sequence(Map<A, ? extends Applicative<B, App>> mapApp) {
        return pure -> (AppMap) Sequence.<B, App, LambdaMap<A, ?>, LambdaMap<A, B>, Applicative<LambdaMap<A, B>, App>>sequence(
                LambdaMap.wrap(mapApp), x -> pure.apply(x.unwrap()).fmap(LambdaMap::wrap))
                .fmap(LambdaMap::unwrap);
    }

    public static <A, B, App extends Applicative<?, App>, AppMap extends Applicative<Map<A, B>, App>>
    AppMap sequence(Map<A, ? extends Applicative<B, App>> mapApp, Fn1<Map<A, B>, ? extends AppMap> pure) {
        return Sequence.<A, B, App, AppMap>sequence(mapApp).apply(pure);
    }
}
