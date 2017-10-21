package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functor.Applicative;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Wrap an {@link Optional} in a {@link Traversable} such that {@link Traversable#traverse(Function, Function)} applies
 * its computation against the value wrapped by the wrapped {@link Optional} (if the {@link Optional} is present).
 * Returns the result of <code>pure</code> if the wrapped {@link Optional} is empty.
 *
 * @param <A> the Optional parameter type
 * @deprecated in favor of {@link LambdaOptional}
 */
@Deprecated
public final class TraversableOptional<A> implements Traversable<A, TraversableOptional> {
    private final Optional<A> delegate;

    @SuppressWarnings("unchecked")
    private TraversableOptional(Optional<? extends A> delegate) {
        this.delegate = (Optional<A>) delegate;
    }

    /**
     * Unwrap the underlying {@link Optional}.
     *
     * @return the wrapped Optional
     */
    public Optional<A> unwrap() {
        return delegate;
    }

    @Override
    public <B> TraversableOptional<B> fmap(Function<? super A, ? extends B> fn) {
        return new TraversableOptional<>(delegate.map(fn));
    }

    @Override
    public <B, App extends Applicative> Applicative<TraversableOptional<B>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, TraversableOptional>, ? extends Applicative<? extends Traversable<B, TraversableOptional>, App>> pure) {
        return fmap(fn).delegate.map(app -> app.fmap(Optional::of).fmap(TraversableOptional::wrap))
                .orElseGet(() -> pure.apply(TraversableOptional.empty()).fmap(x -> (TraversableOptional<B>) x));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof TraversableOptional
                && Objects.equals(delegate, ((TraversableOptional) other).delegate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    /**
     * Wrap an {@link Optional} in a <code>TraversableOptional</code>.
     *
     * @param optional the Optional
     * @param <A>      the Optional parameter type
     * @return the Optional wrapped in a TraversableOptional
     */
    public static <A> TraversableOptional<A> wrap(Optional<? extends A> optional) {
        return new TraversableOptional<>(optional);
    }

    /**
     * Construct an empty <code>TraversableOptional</code> by wrapping {@link Optional#empty()}.
     *
     * @param <A> the optional parameter type
     * @return a TraversableOptional wrapping Optional.empty()
     */
    public static <A> TraversableOptional<A> empty() {
        return TraversableOptional.wrap(Optional.empty());
    }
}
