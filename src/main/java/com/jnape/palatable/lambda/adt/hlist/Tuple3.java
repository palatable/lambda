package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A 3-element tuple product type, implemented as a specialized HList.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @param <_3> The third slot element type
 * @see HList
 * @see Singleton
 * @see Tuple2
 * @see Tuple4
 * @see Tuple5
 */
public final class Tuple3<_1, _2, _3> extends HCons<_1, Tuple2<_2, _3>> implements Functor<_3>, Bifunctor<_2, _3> {
    Tuple3(_1 _1, Tuple2<_2, _3> tail) {
        super(_1, tail);
    }

    @Override
    public <_0> Tuple4<_0, _1, _2, _3> cons(_0 _0) {
        return new Tuple4<>(_0, this);
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

    @Override
    public <_3Prime> Tuple3<_1, _2, _3Prime> fmap(Fn1<? super _3, ? extends _3Prime> fn) {
        return biMapR(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple3<_1, _2Prime, _3> biMapL(Fn1<? super _2, ? extends _2Prime> fn) {
        return (Tuple3<_1, _2Prime, _3>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_3Prime> Tuple3<_1, _2, _3Prime> biMapR(Fn1<? super _3, ? extends _3Prime> fn) {
        return (Tuple3<_1, _2, _3Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_2Prime, _3Prime> Tuple3<_1, _2Prime, _3Prime> biMap(Fn1<? super _2, ? extends _2Prime> lFn,
                                                                 Fn1<? super _3, ? extends _3Prime> rFn) {
        return new Tuple3<>(_1(), tail().biMap(lFn, rFn));
    }
}
