package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;
import java.util.function.Function;

/**
 * A functor over some value of type <code>A</code> that can be mapped over and retrieved later.
 *
 * @param <A> the value type
 */
public final class Identity<A> implements Monad<A, Identity>, Traversable<A, Identity> {

    private final A a;

    public Identity(A a) {
        this.a = a;
    }

    /**
     * Retrieve the value.
     *
     * @return the value
     */
    public A runIdentity() {
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> flatMap(Function<? super A, ? extends Monad<B, Identity>> f) {
        return f.apply(runIdentity()).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> fmap(Function<? super A, ? extends B> fn) {
        return Monad.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> pure(B b) {
        return new Identity<>(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> zip(Applicative<Function<? super A, ? extends B>, Identity> appFn) {
        return new Identity<>(appFn.<Identity<Function<? super A, ? extends B>>>coerce().runIdentity().apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<Identity<B>> lazyZip(
            Lazy<Applicative<Function<? super A, ? extends B>, Identity>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Applicative::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> discardL(Applicative<B, Identity> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<A> discardR(Applicative<B, Identity> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative, TravB extends Traversable<B, Identity>, AppB extends Applicative<B, App>, AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Function<? super A, ? extends AppB> fn, Function<? super TravB, ? extends AppTrav> pure) {
        return (AppTrav) fn.apply(runIdentity()).fmap(Identity::new);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Identity && Objects.equals(a, ((Identity) other).a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "a=" + a +
                '}';
    }
}
