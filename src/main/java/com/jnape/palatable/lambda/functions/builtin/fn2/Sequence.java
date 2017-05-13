package com.jnape.palatable.lambda.functions.builtin.fn2;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * Given a {@link Traversable} of {@link Applicative}s and a pure {@link Applicative} constructor, traverse the elements
 * from left to right, zipping the applicatives together and collecting the results. If the traversable is empty, simply
 * wrap it in the applicative by calling <code>pure</code>.
 * <p>
 * Modulo any type-level coercion, this is equivalent to <code>traversable.traverse(id(), pure)</code>.
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
}
