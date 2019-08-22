package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Cocartesian;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.optics.Prism;

import static com.jnape.palatable.lambda.adt.Either.left;
import static com.jnape.palatable.lambda.adt.choice.Choice2.a;
import static com.jnape.palatable.lambda.adt.choice.Choice2.b;
import static com.jnape.palatable.lambda.functions.Fn1.fn1;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A profunctor used to extract the isomorphic functions a {@link Prism} is composed of.
 *
 * @param <A> the output that might fail to be produced
 * @param <B> the input that guarantees its output
 * @param <S> the input that might fail to map to its output
 * @param <T> the guaranteed output
 */
public final class Market<A, B, S, T> implements
        Monad<T, Market<A, B, S, ?>>,
        Cocartesian<S, T, Market<A, B, ?, ?>> {

    private final Fn1<? super B, ? extends T>            bt;
    private final Fn1<? super S, ? extends Either<T, A>> sta;

    public Market(Fn1<? super B, ? extends T> bt, Fn1<? super S, ? extends Either<T, A>> sta) {
        this.bt = fn1(bt);
        this.sta = fn1(sta);
    }

    /**
     * Extract the mapping <code>B -&gt; T</code>.
     *
     * @return a <code>{@link Fn1}&lt;B, T&gt;</code>
     */
    public Fn1<? super B, ? extends T> bt() {
        return bt;
    }

    /**
     * Extract the mapping <code>S -&gt; {@link Either}&lt;T, A&gt;</code>.
     *
     * @return a <code>{@link Fn1}&lt;S, {@link Either}&lt;T, A&gt;&gt;</code>
     */
    public Fn1<? super S, ? extends Either<T, A>> sta() {
        return sta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Market<A, B, S, U> pure(U u) {
        return new Market<>(constantly(u), constantly(left(u)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Market<A, B, S, U> flatMap(Fn1<? super T, ? extends Monad<U, Market<A, B, S, ?>>> f) {
        return new Market<>(b -> f.apply(bt().apply(b)).<Market<A, B, S, U>>coerce().bt().apply(b),
                            s -> sta().apply(s).invert()
                                    .flatMap(t -> f.apply(t).<Market<A, B, S, U>>coerce().sta()
                                            .apply(s).invert()).invert());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Market<A, B, S, U> zip(Applicative<Fn1<? super T, ? extends U>, Market<A, B, S, ?>> appFn) {
        Market<A, B, S, Fn1<? super T, ? extends U>> marketF = appFn.coerce();
        return new Market<>(b -> marketF.bt().apply(b).apply(bt().apply(b)),
                            s -> sta().apply(s).invert().zip(marketF.sta().apply(s).invert()).invert());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Market<A, B, S, U> fmap(Fn1<? super T, ? extends U> fn) {
        return diMapR(fn::apply);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C> Market<A, B, Choice2<C, S>, Choice2<C, T>> cocartesian() {
        return new Market<>(bt.fmap(Choice2::b),
                            cs -> cs.fmap(sta).match(c -> left(a(c)),
                                                     tOrA -> tOrA.match(t -> left(b(t)), Either::right)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R, U> Market<A, B, R, U> diMap(Fn1<? super R, ? extends S> lFn,
                                           Fn1<? super T, ? extends U> rFn) {
        return new Market<>(bt.fmap(rFn), sta.diMapL(lFn).diMapR(c -> c.biMapL(rFn)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> Market<A, B, R, T> diMapL(Fn1<? super R, ? extends S> fn) {
        return (Market<A, B, R, T>) Cocartesian.super.<R>diMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Market<A, B, S, U> diMapR(Fn1<? super T, ? extends U> fn) {
        return (Market<A, B, S, U>) Cocartesian.super.<U>diMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> Market<A, B, R, T> contraMap(Fn1<? super R, ? extends S> fn) {
        return (Market<A, B, R, T>) Cocartesian.super.<R>contraMap(fn);
    }

    /**
     * The canonical {@link Pure} instance for {@link Market}.
     *
     * @param <A> the output that might fail to be produced
     * @param <B> the input that guarantees its output
     * @param <S> the input that might fail to map to its output
     * @return the {@link Pure} instance
     */
    public static <A, B, S> Pure<Market<A, B, S, ?>> pureMarket() {
        return new Pure<Market<A, B, S, ?>>() {
            @Override
            public <T> Market<A, B, S, T> checkedApply(T t) {
                return new Market<>(constantly(t), constantly(left(t)));
            }
        };
    }
}
