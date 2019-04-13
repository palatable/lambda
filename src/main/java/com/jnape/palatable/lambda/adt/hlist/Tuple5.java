package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functor.Applicative;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.builtin.Lazy;
import com.jnape.palatable.lambda.monad.Monad;
import com.jnape.palatable.lambda.traversable.Traversable;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;

/**
 * A 5-element tuple product type, implemented as a specialized HList. Supports random access.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @see Product5
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 */
public class Tuple5<_1, _2, _3, _4, _5> extends HCons<_1, Tuple4<_2, _3, _4, _5>> implements
        Product5<_1, _2, _3, _4, _5>,
        Monad<_5, Tuple5<_1, _2, _3, _4, ?>>,
        Bifunctor<_4, _5, Tuple5<_1, _2, _3, ?, ?>>,
        Traversable<_5, Tuple5<_1, _2, _3, _4, ?>> {

    private final _1 _1;
    private final _2 _2;
    private final _3 _3;
    private final _4 _4;
    private final _5 _5;

    Tuple5(_1 _1, Tuple4<_2, _3, _4, _5> tail) {
        super(_1, tail);
        this._1 = _1;
        _2 = tail._1();
        _3 = tail._2();
        _4 = tail._3();
        _5 = tail._4();
    }

    @Override
    public <_0> Tuple6<_0, _1, _2, _3, _4, _5> cons(_0 _0) {
        return new Tuple6<>(_0, this);
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
    public Tuple5<_2, _3, _4, _5, _1> rotateL5() {
        return tuple(_2, _3, _4, _5, _1);
    }

    @Override
    public Tuple5<_5, _1, _2, _3, _4> rotateR5() {
        return tuple(_5, _1, _2, _3, _4);
    }

    @Override
    public Tuple5<_2, _3, _4, _1, _5> rotateL4() {
        return tuple(_2, _3, _4, _1, _5);
    }

    @Override
    public Tuple5<_4, _1, _2, _3, _5> rotateR4() {
        return tuple(_4, _1, _2, _3, _5);
    }

    @Override
    public Tuple5<_2, _3, _1, _4, _5> rotateL3() {
        return tuple(_2, _3, _1, _4, _5);
    }

    @Override
    public Tuple5<_3, _1, _2, _4, _5> rotateR3() {
        return tuple(_3, _1, _2, _4, _5);
    }

    @Override
    public Tuple5<_2, _1, _3, _4, _5> invert() {
        return tuple(_2, _1, _3, _4, _5);
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> fmap(Function<? super _5, ? extends _5Prime> fn) {
        return Monad.super.<_5Prime>fmap(fn).coerce();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple5<_1, _2, _3, _4Prime, _5> biMapL(Function<? super _4, ? extends _4Prime> fn) {
        return (Tuple5<_1, _2, _3, _4Prime, _5>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> biMapR(Function<? super _5, ? extends _5Prime> fn) {
        return (Tuple5<_1, _2, _3, _4, _5Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_4Prime, _5Prime> Tuple5<_1, _2, _3, _4Prime, _5Prime> biMap(Function<? super _4, ? extends _4Prime> lFn,
                                                                         Function<? super _5, ? extends _5Prime> rFn) {
        return new Tuple5<>(_1(), tail().biMap(lFn, rFn));
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> pure(_5Prime _5Prime) {
        return tuple(_1, _2, _3, _4, _5Prime);
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> zip(
            Applicative<Function<? super _5, ? extends _5Prime>, Tuple5<_1, _2, _3, _4, ?>> appFn) {
        return Monad.super.zip(appFn).coerce();
    }

    @Override
    public <_5Prime> Lazy<Tuple5<_1, _2, _3, _4, _5Prime>> lazyZip(
            Lazy<Applicative<Function<? super _5, ? extends _5Prime>, Tuple5<_1, _2, _3, _4, ?>>> lazyAppFn) {
        return Monad.super.lazyZip(lazyAppFn).fmap(Applicative::coerce);
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> discardL(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return Monad.super.discardL(appB).coerce();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5> discardR(Applicative<_5Prime, Tuple5<_1, _2, _3, _4, ?>> appB) {
        return Monad.super.discardR(appB).coerce();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> flatMap(
            Function<? super _5, ? extends Monad<_5Prime, Tuple5<_1, _2, _3, _4, ?>>> f) {
        return pure(f.apply(_5).<Tuple5<_1, _2, _3, _4, _5Prime>>coerce()._5());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime, App extends Applicative<?, App>, TravB extends Traversable<_5Prime, Tuple5<_1, _2, _3, _4, ?>>,
            AppB extends Applicative<_5Prime, App>,
            AppTrav extends Applicative<TravB, App>> AppTrav traverse(Function<? super _5, ? extends AppB> fn,
                                                                      Function<? super TravB, ? extends AppTrav> pure) {
        return fn.apply(_5).fmap(_3Prime -> fmap(constantly(_3Prime))).<TravB>fmap(Applicative::coerce).coerce();
    }

    /**
     * Given a value of type <code>A</code>, produced an instance of this tuple with each slot set to that value.
     *
     * @param a   the value to fill the tuple with
     * @param <A> the value type
     * @return the filled tuple
     * @see Tuple2#fill
     */
    public static <A> Tuple5<A, A, A, A, A> fill(A a) {
        return tuple(a, a, a, a, a);
    }
}
