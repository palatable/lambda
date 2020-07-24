package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.bimonad.BimonadRec;
import com.jnape.palatable.lambda.comonad.Comonad;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.Head;
import com.jnape.palatable.lambda.functions.recursion.RecursiveResult;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.monad.MonadRec;
import com.jnape.palatable.lambda.monad.MonadWriter;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.Map;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Both.both;
import static com.jnape.palatable.lambda.functions.recursion.Trampoline.trampoline;

/**
 * A 2-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @see Product2
 * @see HList
 * @see SingletonHList
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple2<_1, _2> extends HCons<_1, SingletonHList<_2>> implements
        Product2<_1, _2>,
        BimonadRec<_2, Tuple2<_1, ?>>,
        MonadWriter<_1, _2, Tuple2<_1, ?>>,
        Bifunctor<_1, _2, Tuple2<?, ?>>,
        Traversable<_2, Tuple2<_1, ?>> {

    private final _1 _1;
    private final _2 _2;

    Tuple2(_1 _1, SingletonHList<_2> tail) {
        super(_1, tail);
        this._1 = _1;
        _2      = tail.head();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_3> Tuple2<_1, Tuple2<_2, _3>> listens(Fn1<? super _1, ? extends _3> fn) {
        return fmap(both(id(), constantly(fn.apply(_1))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2<_1, _2> censor(Fn1<? super _1, ? extends _1> fn) {
        return biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple3<_0, _1, _2> cons(_0 _0) {
        return new Tuple3<>(_0, this);
    }

    /**
     * Snoc an element onto the back of this {@link Tuple2}.
     *
     * @param _3   the new last element
     * @param <_3> the new last element type
     * @return the new {@link Tuple3}
     */
    public <_3> Tuple3<_1, _2, _3> snoc(_3 _3) {
        return tuple(_1, _2, _3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _1 _1() {
        return _1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _2 _2() {
        return _2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _1 getKey() {
        return _1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _2 getValue() {
        return _2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _2 setValue(_2 value) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple2<_2, _1> invert() {
        return tuple(_2, _1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _2 extract() {
        return _2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <B> Tuple2<_1, B> extendImpl(Fn1<? super Comonad<_2, Tuple2<_1, ?>>, ? extends B> f) {
        return tuple(_1, f.apply(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> fmap(Fn1<? super _2, ? extends _2Prime> fn) {
        return BimonadRec.super.<_2Prime>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime> Tuple2<_1Prime, _2> biMapL(Fn1<? super _1, ? extends _1Prime> fn) {
        return (Tuple2<_1Prime, _2>) Bifunctor.super.<_1Prime>biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> biMapR(Fn1<? super _2, ? extends _2Prime> fn) {
        return (Tuple2<_1, _2Prime>) Bifunctor.super.<_2Prime>biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_1Prime, _2Prime> Tuple2<_1Prime, _2Prime> biMap(Fn1<? super _1, ? extends _1Prime> lFn,
                                                             Fn1<? super _2, ? extends _2Prime> rFn) {
        return new Tuple2<>(lFn.apply(_1()), tail().fmap(rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> pure(_2Prime _2Prime) {
        return tuple(_1, _2Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> zip(
            Applicative<Fn1<? super _2, ? extends _2Prime>, Tuple2<_1, ?>> appFn) {
        return BimonadRec.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Lazy<Tuple2<_1, _2Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _2, ? extends _2Prime>, Tuple2<_1, ?>>> lazyAppFn) {
        return BimonadRec.super.lazyZip(lazyAppFn).fmap(Monad<_2Prime, Tuple2<_1, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> discardL(Applicative<_2Prime, Tuple2<_1, ?>> appB) {
        return BimonadRec.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2> discardR(Applicative<_2Prime, Tuple2<_1, ?>> appB) {
        return BimonadRec.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> flatMap(Fn1<? super _2, ? extends Monad<_2Prime, Tuple2<_1, ?>>> f) {
        return pure(f.apply(_2).<Tuple2<_1, _2Prime>>coerce()._2());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime> Tuple2<_1, _2Prime> trampolineM(
            Fn1<? super _2, ? extends MonadRec<RecursiveResult<_2, _2Prime>, Tuple2<_1, ?>>> fn) {
        return fmap(trampoline(x -> fn.apply(x).<Tuple2<_1, RecursiveResult<_2, _2Prime>>>coerce()._2()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_2Prime, App extends Applicative<?, App>, TravB extends Traversable<_2Prime, Tuple2<_1, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Fn1<? super _2, ? extends Applicative<_2Prime, App>> fn,
            Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_2).fmap(_2Prime -> fmap(constantly(_2Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Static factory method for creating <code>Tuple2</code>s from {@link java.util.Map.Entry}s.
     *
     * @param entry the map entry
     * @param <K>   the key parameter type, and first (head) element type
     * @param <V>   the value parameter type, and second element type
     * @return the newly created Tuple2
     */
    public static <K, V> Tuple2<K, V> fromEntry(Map.Entry<K, V> entry) {
        return new Tuple2<>(entry.getKey(), singletonHList(entry.getValue()));
    }

    /**
     * Given a value of type <code>A</code>, produce an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     */
    public static <A> Tuple2<A, A> fill(A a) {
        return tuple(a, a);
    }

    /**
     * Return {@link Maybe#just(Object) just} the first two elements from the given {@link Iterable}, or
     * {@link Maybe#nothing() nothing} if there are less than two elements.
     *
     * @param as  the {@link Iterable}
     * @param <A> the {@link Iterable} element type
     * @return {@link Maybe} the first two elements of the given {@link Iterable}
     */
    public static <A> Maybe<Tuple2<A, A>> fromIterable(Iterable<A> as) {
        return uncons(as).flatMap(tail -> tail.traverse(Head::head, Maybe::just));
    }

    /**
     * The canonical {@link Pure} instance for {@link Tuple2}.
     *
     * @param _1   the head element
     * @param <_1> the head element type
     * @return the {@link Pure} instance
     */
    public static <_1> Pure<Tuple2<_1, ?>> pureTuple(_1 _1) {
        return new Pure<Tuple2<_1, ?>>() {
            @Override
            public <_2> Tuple2<_1, _2> checkedApply(_2 _2) {
                return tuple(_1, _2);
            }
        };
    }
}
