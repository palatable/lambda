package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.comonad.builtin.ComonadEnv;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Id;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Objects;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

/**
 * A concrete implementation of the {@link ComonadEnv} interface.
 *
 * @param <E> the environment type
 * @param <A> the value type
 */
public final class Env<E, A> implements Comonad<A, Env<E, ?>>, ComonadEnv<E, A, Env<E, ?>>, Bifunctor<E, A, Env<?, ?>> {
    private E env;
    private A value;

    private Env(E e, A a) {
        this.env = e;
        this.value = a;
    }


    /**
     * Constructor function for an Env.
     *
     * @param env    the environment provided as context to value
     * @param value  the primary value to be extracted
     * @param <E>    the type of the environment
     * @param <A>    the type of the value
     * @return       a new instance of Env&lt;E, A&gt;
     */
    public static <E, A> Env<E, A> env(E env, A value) {
        return new Env<>(env, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final E ask() {
        return this.env;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R> R asks(Fn1<? super E, ? extends R> f) {
        return f.apply(this.ask());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> Env<R, A> mapEnv(Fn1<? super E, ? extends R> f) {
        return env(f.apply(this.env), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public A extract() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, WA extends Comonad<A, Env<E, ?>>> Env<E, B> extend(Fn1<? super WA, ? extends B> f) {
        return ComonadEnv.super.<B, WA>extend(f).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Comonad<B, Env<E, ?>> extendImpl(Fn1<? super Comonad<A, Env<E, ?>>, ? extends B> f) {
        return env(env, f.apply(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Env<E, B> fmap(Fn1<? super A, ? extends B> fn) {
        return env(env, fn.apply(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> Env<R, A> biMapL(Fn1<? super E, ? extends R> fn) {
        return mapEnv(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Env<E, C> biMapR(Fn1<? super A, ? extends C> fn) {
        return fmap(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R, C> Env<R, C> biMap(Fn1<? super E, ? extends R> lFn, Fn1<? super A, ? extends C> rFn) {
        return env(lFn.apply(env), rFn.apply(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Env && Objects.equals(value, ((Env) other).value) && Objects.equals(env, ((Env) other).env);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(tuple(env, value));
    }
}
