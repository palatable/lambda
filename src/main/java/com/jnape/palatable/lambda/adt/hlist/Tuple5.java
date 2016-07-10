package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A 5-element tuple product type, implemented as a specialized HList.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @param <_5> The fifth slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple2
 * @see Tuple3
 * @see Tuple4
 */
public final class Tuple5<_1, _2, _3, _4, _5> extends HCons<_1, Tuple4<_2, _3, _4, _5>> implements Functor<_5>, Bifunctor<_4, _5> {
    Tuple5(_1 a, Tuple4<_2, _3, _4, _5> tail) {
        super(a, tail);
    }

    @Override
    public <_0> HCons<_0, Tuple5<_1, _2, _3, _4, _5>> cons(_0 _0) {
        return new HCons<>(_0, this);
    }

    public _1 _1() {
        return head();
    }

    public _2 _2() {
        return tail()._1();
    }

    public _3 _3() {
        return tail()._2();
    }

    public _4 _4() {
        return tail()._3();
    }

    public _5 _5() {
        return tail()._4();
    }

    @Override
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> fmap(Fn1<? super _5, ? extends _5Prime> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple5<_1, _2, _3, _4Prime, _5> biMapL(Fn1<? super _4, ? extends _4Prime> fn) {
        return (Tuple5<_1, _2, _3, _4Prime, _5>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_5Prime> Tuple5<_1, _2, _3, _4, _5Prime> biMapR(Fn1<? super _5, ? extends _5Prime> fn) {
        return (Tuple5<_1, _2, _3, _4, _5Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_4Prime, _5Prime> Tuple5<_1, _2, _3, _4Prime, _5Prime> biMap(
            Fn1<? super _4, ? extends _4Prime> lFn,
            Fn1<? super _5, ? extends _5Prime> rFn) {
        return new Tuple5<>(_1(), tail().biMap(lFn, rFn));
    }
}
