package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn2.Into;
import com.jnape.palatable.lambda.functions.specialized.Pure;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Uncons.uncons;

/**
 * A 6-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @param <_6> The sixth slot element type
 * @see Product6
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public class Tuple6<_1, _2, _3, _4, _5, _6> extends HCons<_1, Tuple5<_2, _3, _4, _5, _6>> implements
        Product6<_1, _2, _3, _4, _5, _6>,
        Monad<_6, Tuple6<_1, _2, _3, _4, _5, ?>>,
        Bifunctor<_5, _6, Tuple6<_1, _2, _3, _4, ?, ?>>,
        Traversable<_6, Tuple6<_1, _2, _3, _4, _5, ?>> {

    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;
    private final _5 _5;
    private final _6 _6;

    Tuple6(_1 _1, Tuple5<_2, _3, _4, _5, _6> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
        _4 = tail._3();
        _5 = tail._4();
        _6 = tail._5();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_0> Tuple7<_0, _1, _2, _3, _4, _5, _6> cons(_0 _0) {
        return new Tuple7<>(_0, this);
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
    public _3 _3() {
        return _3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _4 _4() {
        return _4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _5 _5() {
        return _5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public _6 _6() {
        return _6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_2, _3, _4, _5, _6, _1> rotateL6() {
        return tuple(_2, _3, _4, _5, _6, _1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_6, _1, _2, _3, _4, _5> rotateR6() {
        return tuple(_6, _1, _2, _3, _4, _5);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_2, _3, _4, _5, _1, _6> rotateL5() {
        return tuple(_2, _3, _4, _5, _1, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_5, _1, _2, _3, _4, _6> rotateR5() {
        return tuple(_5, _1, _2, _3, _4, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_2, _3, _4, _1, _5, _6> rotateL4() {
        return tuple(_2, _3, _4, _1, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_4, _1, _2, _3, _5, _6> rotateR4() {
        return tuple(_4, _1, _2, _3, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_2, _3, _1, _4, _5, _6> rotateL3() {
        return tuple(_2, _3, _1, _4, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_3, _1, _2, _4, _5, _6> rotateR3() {
        return tuple(_3, _1, _2, _4, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple6<_2, _1, _3, _4, _5, _6> invert() {
        return tuple(_2, _1, _3, _4, _5, _6);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> fmap(Fn1<? super _6, ? extends _6Prime> fn) {
        return Monad.super.<_6Prime>fmap(fn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime> Tuple6<_1, _2, _3, _4, _5Prime, _6> biMapL(Fn1<? super _5, ? extends _5Prime> fn) {
        return (Tuple6<_1, _2, _3, _4, _5Prime, _6>) Bifunctor.super.biMapL(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> biMapR(Fn1<? super _6, ? extends _6Prime> fn) {
        return (Tuple6<_1, _2, _3, _4, _5, _6Prime>) Bifunctor.super.biMapR(fn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_5Prime, _6Prime> Tuple6<_1, _2, _3, _4, _5Prime, _6Prime> biMap(
            Fn1<? super _5, ? extends _5Prime> lFn,
            Fn1<? super _6, ? extends _6Prime> rFn) {
        return new Tuple6<>(_1(), tail().biMap(lFn, rFn));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> pure(_6Prime _6Prime) {
        return tuple(_1, _2, _3, _4, _5, _6Prime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> zip(
            Applicative<Fn1<? super _6, ? extends _6Prime>, Tuple6<_1, _2, _3, _4, _5, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Lazy<Tuple6<_1, _2, _3, _4, _5, _6Prime>> lazyZip(
            Lazy<? extends Applicative<Fn1<? super _6, ? extends _6Prime>, Tuple6<_1, _2, _3, _4, _5, ?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>::coerce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> discardL(
            Applicative<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6> discardR(Applicative<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> flatMap(
            Fn1<? super _6, ? extends Monad<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>> f) {
        return pure(f.apply(_6).<Tuple6<_1, _2, _3, _4, _5, _6Prime>>coerce()._6());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <_6Prime, App extends Applicative<?, App>, TravB extends Traversable<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(
            Fn1<? super _6, ? extends Applicative<_6Prime, App>> fn,
            Fn1<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_6).fmap(_6Prime -> fmap(constantly(_6Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple6<A, A, A, A, A, A> fill(A a) {
        return tuple(a, a, a, a, a, a);
    }

    /**
     * Return {@link Maybe#just(Object) just} the first six elements from the given {@link Iterable}, or
     * {@link Maybe#nothing() nothing} if there are less than six elements.
     *
     * @param as  the {@link Iterable}
     * @param <A> the {@link Iterable} element type
     * @return {@link Maybe} the first six elements of the given {@link Iterable}
     */
    public static <A> Maybe<Tuple6<A, A, A, A, A, A>> fromIterable(Iterable<A> as) {
        return uncons(as).flatMap(Into.into((head, tail) -> Tuple5.fromIterable(tail).fmap(t -> t.cons(head))));
    }

    /**
     * The canonical {@link Pure} instance for {@link Tuple6}.
     *
     * @param _1   the head element
     * @param _2   the second element
     * @param _3   the third element
     * @param _4   the fourth element
     * @param _5   the fifth element
     * @param <_1> the head element type
     * @param <_2> the second element type
     * @param <_3> the third element type
     * @param <_4> the fourth element type
     * @param <_5> the fifth element type
     * @return the {@link Pure} instance
     */
    public static <_1, _2, _3, _4, _5> Pure<Tuple6<_1, _2, _3, _4, _5, ?>> pureTuple(_1 _1, _2 _2, _3 _3, _4 _4,
                                                                                     _5 _5) {
        return new Pure<Tuple6<_1, _2, _3, _4, _5, ?>>() {
            @Override
            public <_6> Tuple6<_1, _2, _3, _4, _5, _6> checkedApply(_6 _6) {
                return tuple(_1, _2, _3, _4, _5, _6);
            }
        };
    }
}
