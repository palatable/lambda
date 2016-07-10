package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A 4-element tuple product type, implemented as a specialized HList.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @param <_4> The fourth slot element type
 * @see HList
 * @see Singleton
 * @see Tuple2
 * @see Tuple3
 * @see Tuple5
 */
public final class Tuple4<_1, _2, _3, _4> extends HCons<_1, Tuple3<_2, _3, _4>> implements Functor<_4>, Bifunctor<_3, _4> {
    Tuple4(_1 _1, Tuple3<_2, _3, _4> tail) {
        super(_1, tail);
    }

    @Override
    public <_0> Tuple5<_0, _1, _2, _3, _4> cons(_0 _0) {
        return new Tuple5<>(_0, this);
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

    @Override
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> fmap(Fn1<? super _4, ? extends _4Prime> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple4<_1, _2, _3Prime, _4> biMapL(Fn1<? super _3, ? extends _3Prime> fn) {
        return (Tuple4<_1, _2, _3Prime, _4>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_4Prime> Tuple4<_1, _2, _3, _4Prime> biMapR(Fn1<? super _4, ? extends _4Prime> fn) {
        return (Tuple4<_1, _2, _3, _4Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_3Prime, _4Prime> Tuple4<_1, _2, _3Prime, _4Prime> biMap(Fn1<? super _3, ? extends _3Prime> lFn,
                                                                     Fn1<? super _4, ? extends _4Prime> rFn) {
        return new Tuple4<>(_1(), tail().biMap(lFn, rFn));
    }
}
