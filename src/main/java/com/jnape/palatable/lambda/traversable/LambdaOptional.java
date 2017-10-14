package com.jnape.palatable.lambda.traversable;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Functor;
import com.jnape.palatable.lambda.monad.Monad;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Adapt an {@link Optional} to support generic lambda interfaces like {@link Functor}, {@link Applicative},
 * {@link Traversable}, etc.
 *
 * @param <A> the Optional parameter type
 */
public final class LambdaOptional<A> implements Monad<A, LambdaOptional>, Traversable<A, LambdaOptional> {
    private final Optional<A> delegate;

    @SuppressWarnings("unchecked")
    private LambdaOptional(Optional<? extends A> delegate) {
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
    public <B> LambdaOptional<B> fmap(Function<? super A, ? extends B> fn) {
        return new LambdaOptional<>(delegate.map(fn));
    }

    @Override
    public <B> LambdaOptional<B> pure(B b) {
        return wrap(Optional.of(b));
    }

    @Override
    public <B> LambdaOptional<B> zip(Applicative<Function<? super A, ? extends B>, LambdaOptional> appFn) {
        return wrap(appFn.<LambdaOptional<Function<? super A, ? extends B>>>coerce().unwrap().flatMap(delegate::map));
    }

    @Override
    public <B> LambdaOptional<B> discardL(Applicative<B, LambdaOptional> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <B> LambdaOptional<A> discardR(Applicative<B, LambdaOptional> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <B> LambdaOptional<B> flatMap(Function<? super A, ? extends Monad<B, LambdaOptional>> f) {
        return wrap(delegate.flatMap(a -> f.apply(a).<LambdaOptional<B>>coerce().unwrap()));
    }

    @Override
    public <B, App extends Applicative> Applicative<LambdaOptional<B>, App> traverse(
            Function<? super A, ? extends Applicative<B, App>> fn,
            Function<? super Traversable<B, LambdaOptional>, ? extends Applicative<? extends Traversable<B, LambdaOptional>, App>> pure) {
        return fmap(fn).delegate.map(app -> app.fmap(Optional::of).fmap(LambdaOptional::wrap))
                .orElseGet(() -> pure.apply(LambdaOptional.empty()).fmap(x -> (LambdaOptional<B>) x));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LambdaOptional
                && Objects.equals(delegate, ((LambdaOptional) other).delegate);
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
    public static <A> LambdaOptional<A> wrap(Optional<? extends A> optional) {
        return new LambdaOptional<>(optional);
    }

    /**
     * Construct an empty <code>TraversableOptional</code> by wrapping {@link Optional#empty()}.
     *
     * @param <A> the optional parameter type
     * @return a TraversableOptional wrapping Optional.empty()
     */
    public static <A> LambdaOptional<A> empty() {
        return LambdaOptional.wrap(Optional.empty());
    }
}
