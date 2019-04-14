package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

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

    @Override
    public <_0> Tuple7<_0, _1, _2, _3, _4, _5, _6> cons(_0 _0) {
        return new Tuple7<>(_0, this);
    }

    @Override
    public _1 _1() {
        return _1;
    }

    @Override
    public _2 _2() {
        return _2;
    }

    @Override
    public _3 _3() {
        return _3;
    }

    @Override
    public _4 _4() {
        return _4;
    }

    @Override
    public _5 _5() {
        return _5;
    }

    @Override
    public _6 _6() {
        return _6;
    }

    @Override
    public Tuple6<_2, _3, _4, _5, _6, _1> rotateL6() {
        return tuple(_2, _3, _4, _5, _6, _1);
    }

    @Override
    public Tuple6<_6, _1, _2, _3, _4, _5> rotateR6() {
        return tuple(_6, _1, _2, _3, _4, _5);
    }

    @Override
    public Tuple6<_2, _3, _4, _5, _1, _6> rotateL5() {
        return tuple(_2, _3, _4, _5, _1, _6);
    }

    @Override
    public Tuple6<_5, _1, _2, _3, _4, _6> rotateR5() {
        return tuple(_5, _1, _2, _3, _4, _6);
    }

    @Override
    public Tuple6<_2, _3, _4, _1, _5, _6> rotateL4() {
        return tuple(_2, _3, _4, _1, _5, _6);
    }

    @Override
    public Tuple6<_4, _1, _2, _3, _5, _6> rotateR4() {
        return tuple(_4, _1, _2, _3, _5, _6);
    }

    @Override
    public Tuple6<_2, _3, _1, _4, _5, _6> rotateL3() {
        return tuple(_2, _3, _1, _4, _5, _6);
    }

    @Override
    public Tuple6<_3, _1, _2, _4, _5, _6> rotateR3() {
        return tuple(_3, _1, _2, _4, _5, _6);
    }

    @Override
    public Tuple6<_2, _1, _3, _4, _5, _6> invert() {
        return tuple(_2, _1, _3, _4, _5, _6);
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> fmap(Function<? super _6, ? extends _6Prime> fn) {
        return Monad.super.<_6Prime>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime> Tuple6<_1, _2, _3, _4, _5Prime, _6> biMapL(Function<? super _5, ? extends _5Prime> fn) {
        return (Tuple6<_1, _2, _3, _4, _5Prime, _6>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> biMapR(Function<? super _6, ? extends _6Prime> fn) {
        return (Tuple6<_1, _2, _3, _4, _5, _6Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_5Prime, _6Prime> Tuple6<_1, _2, _3, _4, _5Prime, _6Prime> biMap(
            Function<? super _5, ? extends _5Prime> lFn,
            Function<? super _6, ? extends _6Prime> rFn) {
        return new Tuple6<>(_1(), tail().biMap(lFn, rFn));
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> pure(_6Prime _6Prime) {
        return tuple(_1, _2, _3, _4, _5, _6Prime);
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> zip(
            Applicative<Function<? super _6, ? extends _6Prime>, Tuple6<_1, _2, _3, _4, _5, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <_6Prime> Lazy<Tuple6<_1, _2, _3, _4, _5, _6Prime>> lazyZip(
            Lazy<? extends Applicative<Function<? super _6, ? extends _6Prime>, Tuple6<_1, _2, _3, _4, _5, ?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Monad<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>::coerce);
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> discardL(
            Applicative<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6> discardR(Applicative<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_6Prime> Tuple6<_1, _2, _3, _4, _5, _6Prime> flatMap(
            Function<? super _6, ? extends Monad<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>> f) {
        return pure(f.apply(_6).<Tuple6<_1, _2, _3, _4, _5, _6Prime>>coerce()._6());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_6Prime, App extends Applicative<?, App>, TravB extends Traversable<_6Prime, Tuple6<_1, _2, _3, _4, _5, ?>>,
            AppB extends Applicative<_6Prime, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super _6, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
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
}
