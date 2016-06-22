package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.MonadicFunction;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

/**
 * A 2-element tuple product type, implemented as a specialized HList.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @see HList
 * @see Singleton
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public final class Tuple2<_1, _2> extends HCons<_1, Singleton<_2>> implements Functor<_2>, Bifunctor<_1, _2> {

    Tuple2(_1 _1, Singleton<_2> tail) {
        super(_1, tail);
    }

    @Override
    public <_0> Tuple3<_0, _1, _2> cons(_0 _0) {
        return new Tuple3<>(_0, this);
    }

    public _1 _1() {
        return head();
    }

    public _2 _2() {
        return tail().head();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> fmap(MonadicFunction<? super _2, ? extends _2Prime> fn) {
        return tuple(_1(), fn.apply(_2()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1Prime> Tuple2<_1Prime, _2> biMapL(MonadicFunction<? super _1, ? extends _1Prime> fn) {
        return (Tuple2<_1Prime, _2>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple2<_1, _2Prime> biMapR(MonadicFunction<? super _2, ? extends _2Prime> fn) {
        return (Tuple2<_1, _2Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_1Prime, _2Prime> Tuple2<_1Prime, _2Prime> biMap(MonadicFunction<? super _1, ? extends _1Prime> lFn,
                                                             MonadicFunction<? super _2, ? extends _2Prime> rFn) {
        return new Tuple2<>(lFn.apply(_1()), tail().fmap(rFn));
    }
}
