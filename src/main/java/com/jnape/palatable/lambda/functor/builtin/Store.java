package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.comonad.builtin.ComonadStore;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A concrete implementation of the {@link ComonadStore} interface.
 *
 * @param <S>  the store type
 * @param <A>  the retrieved value type
 */
public final class Store<S, A> implements Comonad<A, Store<S, ?>>, ComonadStore<S, A, Store<S, ?>> {
    private Fn1<? super S, ? extends A> storage;
    private S cursor;

    private Store(Fn1<? super S, ? extends A> f, S s) {
        this.storage = f;
        this.cursor = s;
    }

    /**
     * Constructor function for Store
     *
     * @param f    the lookup function from storage to a retrieved value
     * @param s    the current cursor for looking up a value from f
     * @param <S>  the type of the storage
     * @param <A>  the type of the retrieved value
     * @return     a new instance of Store&lt;S, A&gt;
     */
    public static <S, A> Store<S, A> store(Fn1<? super S, ? extends A> f, S s) {
        return new Store<>(f, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final A peeks(Fn1<? super S, ? extends S> f) {
        return storage.contraMap(f).apply(cursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Store<S, A> seeks(Fn1<? super S, ? extends S> f) {
        return store(storage, f.apply(cursor));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <F extends Functor<?, F>> Functor<A, F> experiment(Fn1<? super S, ? extends Functor<S, F>> f) {
        return f.apply(cursor).fmap(c -> peeks(constantly(c)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Store<S, B> fmap(Fn1<? super A, ? extends B> fn) {
        return ComonadStore.super.<B>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Comonad<B, Store<S, ?>> extendImpl(Fn1<? super Comonad<A, Store<S, ?>>, ? extends B> f) {
        return store(s -> f.apply(store(storage, s)), cursor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B, WA extends Comonad<A, Store<S, ?>>> Store<S, B> extend(Fn1<? super WA, ? extends B> f) {
        return ComonadStore.super.<B, WA>extend(f).coerce();
    }
}
