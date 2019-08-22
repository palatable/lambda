package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Objects;

/**
 * A functor over some value of type <code>A</code> that can be mapped over and retrieved later.
 *
 * @param <A> the value type
 */
public final class Identity<A> implements Monad<A, Identity<?>>, Traversable<A, Identity<?>> {

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
    public <B> Identity<B> flatMap(Fn1<? super A, ? extends Monad<B, Identity<?>>> f) {
        return f.apply(runIdentity()).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> fmap(Fn1<? super A, ? extends B> fn) {
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
    public <B> Identity<B> zip(Applicative<Fn1<? super A, ? extends B>, Identity<?>> appFn) {
        return new Identity<>(appFn.<Identity<Fn1<? super A, ? extends B>>>coerce().runIdentity().apply(a));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Lazy<Identity<B>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super A, ? extends B>, Identity<?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<B, Identity<?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<B> discardL(Applicative<B, Identity<?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Identity<A> discardR(Applicative<B, Identity<?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <B, App extends Applicative<?, App>, TravB extends Traversable<B, Identity<?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Fn1<? super A, ? extends Applicative<B, App>> fn,
                                                                      Fn1<? super TravB, ? extends AppTrav> pure) {
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

    /**
     * The canonical {@link Pure} instance for {@link Identity}.
     *
     * @return the {@link Pure} instance
     */
    public static Pure<Identity<?>> pureIdentity() {
        return Identity::new;
    }
}
