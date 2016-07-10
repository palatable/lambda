package com.jnape.palatable.lambda.adt.hlist;

import com.jnape.palatable.lambda.adt.hlist.HList.HCons;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.Map;

/**
 * A 2-element tuple product type, implemented as a specialized HList.
 *
 * @param <_1> The first slot element type
 * @param <_2> The second slot element type
 * @see HList
 * @see SingletonHList
 * @see Tuple3
 * @see Tuple4
 * @see Tuple5
 */
public final class Tuple2<_1, _2> extends HCons<_1, SingletonHList<_2>> implements Map.Entry<_1, _2>, Functor<_2>, Bifunctor<_1, _2> {

    Tuple2(_1 _1, SingletonHList<_2> tail) {
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
    public _1 getKey() {
        return _1();
    }

    @Override
    public _2 getValue() {
        return _2();
    }

    @Override
    public _2 setValue(_2 value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <_2Prime> Tuple2<_1, _2Prime> fmap(Fn1<? super _2, ? extends _2Prime> fn) {
        return tuple(_1(), fn.apply(_2()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_1Prime> Tuple2<_1Prime, _2> biMapL(Fn1<? super _1, ? extends _1Prime> fn) {
        return (Tuple2<_1Prime, _2>) Bifunctor.super.biMapL(fn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <_2Prime> Tuple2<_1, _2Prime> biMapR(Fn1<? super _2, ? extends _2Prime> fn) {
        return (Tuple2<_1, _2Prime>) Bifunctor.super.biMapR(fn);
    }

    @Override
    public <_1Prime, _2Prime> Tuple2<_1Prime, _2Prime> biMap(Fn1<? super _1, ? extends _1Prime> lFn,
                                                             Fn1<? super _2, ? extends _2Prime> rFn) {
        return new Tuple2<>(lFn.apply(_1()), tail().fmap(rFn));
    }

    public static <K, V> Tuple2<K, V> fromEntry(Map.Entry<K, V> entry) {
        return new Tuple2<>(entry.getKey(), singletonHList(entry.getValue()));
    }
}
