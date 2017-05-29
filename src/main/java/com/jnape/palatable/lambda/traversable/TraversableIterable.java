package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functor.Applicative;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Map.map;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldRight.foldRight;
import static java.util.Collections.emptyList;

/**
 * Wrap an {@link Iterable} in a {@link Traversable} such that {@link Traversable#traverse(Function, Function)} applies
 * its computation against each element of the wrapped {@link Iterable}. Returns the result of <code>pure</code> if the
 * wrapped {@link Iterable} is empty.
 *
 * @param <A> the Iterable element type
 */
public final class TraversableIterable<A> implements Traversable<A, TraversableIterable> {
    private final Iterable<A> as;

    @SuppressWarnings("unchecked")
    private TraversableIterable(Iterable<? extends A> as) {
        this.as = (Iterable<A>) as;
    }

    /**
     * Unwrap the underlying {@link Iterable}.
     *
     * @return the wrapped Iterable
     */
    public Iterable<A> unwrap() {
        return as;
    }

    @Override
    public <B> TraversableIterable<B> fmap(Function<? super A, ? extends B> fn) {
        return wrap(map(fn, as));
    }

    @Override
    public <B, App extends Applicative> Applicative<TraversableIterable<B>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, TraversableIterable>, ? extends Applicative<? extends Traversable<B, TraversableIterable>, App>> pure) {
        return foldRight((a, appTrav) -> appTrav.zip(fn.apply(a).fmap(b -> bs -> TraversableIterable.<B>wrap(cons(b, bs.unwrap())))),
                         pure.apply(TraversableIterable.empty()).fmap(ti -> (TraversableIterable<B>) ti),
                         as);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TraversableIterable) {
            Iterator<A> xs = as.iterator();
            Iterator ys = ((TraversableIterable) other).as.iterator();

            while (xs.hasNext() && ys.hasNext())
                if (!Objects.equals(xs.next(), ys.next()))
                    return false;

            return xs.hasNext() == ys.hasNext();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(as);
    }

    /**
     * Wrap an {@link Iterable} in a <code>TraversableIterable</code>.
     *
     * @param as  the Iterable
     * @param <A> the Iterable element type
     * @return the Iterable wrapped in a TraversableIterable
     */
    public static <A> TraversableIterable<A> wrap(Iterable<? extends A> as) {
        return new TraversableIterable<>(as);
    }

    /**
     * Construct an empty <code>TraversableIterable</code> by wrapping {@link java.util.Collections#emptyList()}.
     *
     * @param <A> the Iterable element type
     * @return a TraversableIterable wrapping Collections.emptyList()
     */
    public static <A> TraversableIterable<A> empty() {
        return wrap(emptyList());
    }
}
